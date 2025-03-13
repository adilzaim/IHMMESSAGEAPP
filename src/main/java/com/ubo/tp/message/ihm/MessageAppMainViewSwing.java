package com.ubo.tp.message.ihm;

import com.ubo.tp.message.ihm.searchUser.SearchUserViewSwing;
import com.ubo.tp.message.ihm.ListUserComponent.MainViewSwing;
import com.ubo.tp.message.ihm.listener.ExitListener;
import com.ubo.tp.message.ihm.messageComponent.MessagePanelSwing;
import com.ubo.tp.message.ihm.userComponent.UserMapViewSwing;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainViewSwing extends JFrame {

    private String selectedDirectory; // Stocke le répertoire sélectionné
    private ExitListener exitListener;
    private LoginViewSwing component;
    private JSplitPane mainSplitPane;
    private JSplitPane bottomSplitPane;
    private JPanel loginContainer;
    private MessagePanelSwing messagePanel;
    private JPanel rightBottomContainer;



    /**
     * Définit l'écouteur de sortie de l'application.
     */
    public void setListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }

    /**
     * Définit le composant de login.
     */
    public void setComponent(LoginViewSwing component) {
        this.component = component;
    }

    /**
     * Initialise le composant de login.
     */
    private void initializeLoginComponent() {
        loginContainer.add(component, BorderLayout.CENTER);
    }

    /**
     * Constructeur principal.
     */
    public MessageAppMainViewSwing(String directory, LoginViewSwing loginView) {
        super("MessageApp - " + directory);

        this.component = loginView;

        // Chargement et définition de l'icône de l'application
        setApplicationIcon();

        // Configuration de la fenêtre
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre

        // Application d'un Look & Feel agréable
        applyLookAndFeel();

        // Ajout de la barre de menu
        setJMenuBar(createMenuBar());

        // Initialisation du conteneur de login
        loginContainer = new JPanel(new BorderLayout());

        // Initialisation du conteneur bottom-right
        rightBottomContainer = new JPanel(new BorderLayout());

        // Initialisation du split pane horizontal pour le bas
        bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomSplitPane.setLeftComponent(null);  // Message panel sera ajouté ici
        bottomSplitPane.setRightComponent(rightBottomContainer);
        bottomSplitPane.setResizeWeight(0.7);  // Message panel prendra 70% de la largeur
        bottomSplitPane.setDividerLocation(500);

        // Initialisation du split pane vertical principal
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setTopComponent(loginContainer);
        mainSplitPane.setBottomComponent(bottomSplitPane);
        mainSplitPane.setResizeWeight(0.8); // La partie supérieure occupe 80% de l'espace
        mainSplitPane.setDividerLocation(480); // Position initiale du diviseur

        // Initialiser le composant de login
        initializeLoginComponent();

        // Définir le split pane comme contenu principal
        setContentPane(mainSplitPane);
    }

    /**
     * Applique un Look & Feel plus moderne.
     */
    private void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("⚠ Impossible de charger le Look & Feel.");
        }
    }

    /**
     * Définit l'icône de l'application.
     */
    private void setApplicationIcon() {
        ImageIcon iconURL = new ImageIcon("src/main/resources/images/logo_20.png");

        if (iconURL.getImage() != null) {
            setIconImage(iconURL.getImage());
        } else {
            System.err.println("⚠ Erreur : Impossible de charger l'icône de l'application.");
        }
    }

    /**
     * Crée la barre de menu de l'application.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu "Fichier"
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter", new ImageIcon("src/main/resources/images/exitIcon_20.png"));
        // Modification de l'action de quitter
        exitItem.addActionListener(e -> {
            if (exitListener != null) {
                exitListener.onExit();
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Menu "Aide"
        JMenu helpMenu = new JMenu("?");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * Affiche la boîte de dialogue "À propos".
     */
    private void showAboutDialog() {
        ImageIcon icon = new ImageIcon("src/main/resources/images/logo_50.png");

        // Créer un JPanel pour contenir le texte
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Créer un JLabel avec le texte centré
        JLabel label = new JLabel("<html><div style='text-align: center;'>UBO M2-TILL<br>Département Informatique</div></html>");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajouter le label au panel
        panel.add(label);

        // Afficher la boîte de dialogue avec le texte centré
        JOptionPane.showMessageDialog(this, panel, "À propos", JOptionPane.INFORMATION_MESSAGE, icon);
    }

    /**
     * Ouvre un sélecteur de fichier pour choisir un répertoire.
     */
    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Sélectionnez un répertoire d'échange");

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = chooser.getSelectedFile().getAbsolutePath();
            JOptionPane.showMessageDialog(this, "Répertoire sélectionné : " + selectedDirectory,
                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            setTitle("MessageApp - " + selectedDirectory);
        }
    }

    /**
     * Affiche la structure des dossiers du répertoire sélectionné.
     */
    private void showDirectoryStructure() {
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(this, "Aucun répertoire sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Path currentDir = Paths.get(selectedDirectory);
            String structure = Files.walk(currentDir, 2) // Limite la profondeur à 2
                    .map(path -> formatPath(currentDir, path))
                    .collect(Collectors.joining("\n"));

            JTextArea textArea = new JTextArea(structure, 20, 50);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(this, scrollPane, "Structure du répertoire sélectionné", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du répertoire", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Formate un chemin pour l'affichage.
     */
    private String formatPath(Path base, Path path) {
        String relativePath = base.relativize(path).toString();
        return path.toFile().isDirectory() ? "[📂] " + relativePath : "  - " + relativePath;
    }

    /**
     * Sélectionne un répertoire au démarrage de l'application.
     */
    public static String chooseDirectoryOnStartup() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Sélectionnez un répertoire de travail");

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null; // Retourne null si l'utilisateur annule
    }


    /**
     * Revient à la vue de login.
     */
    public void setLoginView() {
        // Revenir à la vue de login
        loginContainer.removeAll();
        loginContainer.add(component, BorderLayout.CENTER);

        // Supprimer le panel de messages s'il existe
        if (messagePanel != null) {
            bottomSplitPane.setLeftComponent(null);
        }

        // Supprimer le composant de droite s'il existe
        rightBottomContainer.removeAll();

        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }

    /**
     * Définit la vue de la carte des utilisateurs.
     */
    public void setUserMapView(UserMapViewSwing userMapView) {
        // Remplacer le contenu de la partie supérieure
        loginContainer.removeAll();
        loginContainer.add(userMapView, BorderLayout.CENTER);
        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }


    /**
     * Définit la vue des messages.
     */
    public void setMessageView(MessagePanelSwing messagePanel) {
        // Stocker la référence du panel de messages
        this.messagePanel = messagePanel;

        // Ajouter le panel de messages dans la partie gauche inférieure
        bottomSplitPane.setLeftComponent(messagePanel);

        // Ajuster la position du diviseur pour montrer le panel de messages
        bottomSplitPane.setDividerLocation(500);

        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }

    /**
     * Ajoute un composant à la zone bottom-right.
     */
    public void setRightBottomComponent(JComponent component) {
        rightBottomContainer.removeAll();
        rightBottomContainer.add(component, BorderLayout.CENTER);

        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }

    public void setUserListView(MainViewSwing mainView) {
        // Supprimer le composant existant dans la partie supérieure
        loginContainer.removeAll();

        // Ajouter la vue des utilisateurs dans la partie supérieure
        loginContainer.add(mainView, BorderLayout.CENTER);

        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }

    public void setSearchUser(SearchUserViewSwing searchUserView) {
        // Supprimer le composant existant dans la partie supérieure
        loginContainer.removeAll();

        // Ajouter la vue des utilisateurs dans la partie supérieure
        loginContainer.add(searchUserView, BorderLayout.CENTER);

        // Forcer la mise à jour de l'affichage
        revalidate();
        repaint();
    }
}