package main.java.com.ubo.tp.message.ihm.ListUserComponent;

import main.java.com.ubo.tp.message.datamodel.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView extends JPanel implements ModelObserver {

    private JPanel leftPanel;
    private JPanel rightPanel;
    private User currentUser;
    private ListListener listListener;
    private ModelData modelData;
    private JButton backButton; // Déclaration du bouton de retour

    public MainView(User currentUser, ModelData modelData) {
        this.modelData = modelData;
        this.modelData.addObserver(this);
        this.currentUser = currentUser;
        setLayout(new BorderLayout());

        // Création des panneaux avec titre
        JPanel leftContainerPanel = createTitledPanel("Utilisateurs disponibles");
        JPanel rightContainerPanel = createTitledPanel("Utilisateurs suivis");

        // Création des panneaux de contenu
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        // Utilisation du layout BoxLayout pour afficher les éléments
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Ajouter les panneaux de contenu aux panneaux avec titre
        leftContainerPanel.add(new JScrollPane(leftPanel), BorderLayout.CENTER);
        rightContainerPanel.add(new JScrollPane(rightPanel), BorderLayout.CENTER);

        // Créer un panneau central pour contenir les deux panneaux avec un diviseur
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftContainerPanel,
                rightContainerPanel
        );
        splitPane.setDividerLocation(350); // Position initiale du diviseur
        splitPane.setOneTouchExpandable(true); // Permet d'étendre un panneau avec un clic

        // Ajouter le SplitPane à la fenêtre principale
        add(splitPane, BorderLayout.CENTER);

        // Création du bouton "Retour" et ajout à la partie inférieure
        backButton = new JButton("Retour");
        backButton.addActionListener(e -> onBackButtonClick());

        JPanel bottomPanel = new JPanel(); // Panneau pour contenir le bouton retour
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Ajouter le panneau de retour en bas de la fenêtre
        add(bottomPanel, BorderLayout.SOUTH);

        // Remplir les listes avec les utilisateurs
        updateUserLists();
    }

    // Méthode pour créer un panneau avec titre
    private JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }

    // Méthode pour gérer l'événement du bouton "Retour"
    private void onBackButtonClick() {
        this.listListener.back();
    }

    // Met à jour les listes des utilisateurs dans les panneaux
    public void updateUserLists() {
        leftPanel.removeAll();
        rightPanel.removeAll();

        // Récupérer les utilisateurs non suivis
        List<User> allUsers = modelData.getAllUserNotfollowed();

        for (User user : allUsers) {
            JPanel userPanel = createUserPanel(user, "Follow");
            leftPanel.add(userPanel);
            // Ajouter un séparateur entre chaque utilisateur
            leftPanel.add(createSeparator());
        }

        // Récupérer les utilisateurs suivis
        List<User> followedUsers = modelData.getFollowed();

        for (User user : followedUsers) {
            JPanel userPanel = createUserPanel(user, "Unfollow");
            rightPanel.add(userPanel);
            // Ajouter un séparateur entre chaque utilisateur
            rightPanel.add(createSeparator());
        }

        // Revalidate et repaint pour mettre à jour l'affichage
        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // Créer un séparateur horizontal
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    // Crée un panneau pour afficher un utilisateur avec son avatar et son tag
    private JPanel createUserPanel(User user, String buttonLabel) {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Charger l'avatar
        ImageIcon avatarIcon = new ImageIcon(user.getAvatarPath());
        // Redimensionner l'avatar pour une taille uniforme
        Image image = avatarIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        avatarIcon = new ImageIcon(image);
        JLabel avatarLabel = new JLabel(avatarIcon);

        // Créer le tag de l'utilisateur
        JLabel userTagLabel = new JLabel("@" + user.getUserTag());
        userTagLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Créer le bouton d'abonnement/désabonnement
        JButton actionButton = new JButton(buttonLabel);
        actionButton.addActionListener(e -> {
            if (buttonLabel.equals("Follow")) {
                this.listListener.follow(user);
            } else {
                this.listListener.unFollow(user);
            }
        });

        // Ajouter les composants au panneau
        userPanel.add(avatarLabel);
        userPanel.add(userTagLabel);
        userPanel.add(actionButton);

        return userPanel;
    }

    @Override
    public void update() {

        updateUserLists();
    }

    public void setListener(ListListener listListener) {
        this.listListener = listListener;
    }
}