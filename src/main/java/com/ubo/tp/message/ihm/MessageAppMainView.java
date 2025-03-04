package main.java.com.ubo.tp.message.ihm;

import main.java.com.ubo.tp.message.core.EntityManager;
import main.java.com.ubo.tp.message.core.database.Database;
import main.java.com.ubo.tp.message.core.database.IDatabase;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainView extends JFrame {

    private String selectedDirectory; // Stocke le répertoire sélectionné

    /**
     * Constructeur principal.
     */
    public MessageAppMainView(String directory) {
        super("MessageApp - " + directory);


        // Chargement et définition de l'icône de l'application (conservant ta config)
        setApplicationIcon();

        // Configuration de la fenêtre
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre

        // Application d'un Look & Feel agréable
        applyLookAndFeel();

        // Ajout de la barre de menu
        setJMenuBar(createMenuBar());


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
     * Définit l'icône de l'application (garde ta configuration).
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
        exitItem.addActionListener(e -> System.exit(0));
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

}
