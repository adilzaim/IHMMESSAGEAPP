package com.ubo.tp.message.ihm.ListUserComponent;

import com.ubo.tp.message.datamodel.User;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.List;

public class MainView extends BorderPane implements ModelObserver {

    private VBox leftPanel;
    private VBox rightPanel;
    private User currentUser;
    private ListListener listListener;
    private ModelData modelData;
    private Button backButton; // Déclaration du bouton de retour

    public MainView(User currentUser, ModelData modelData) {
        this.modelData = modelData;
        this.modelData.addObserver(this);
        this.currentUser = currentUser;

        // Création des panneaux avec titre
        BorderPane leftContainerPanel = createTitledPanel("Utilisateurs disponibles");
        BorderPane rightContainerPanel = createTitledPanel("Utilisateurs suivis");

        // Création des panneaux de contenu
        leftPanel = new VBox();
        leftPanel.setSpacing(5);
        leftPanel.setPadding(new Insets(5));

        rightPanel = new VBox();
        rightPanel.setSpacing(5);
        rightPanel.setPadding(new Insets(5));

        // Ajouter les panneaux de contenu aux panneaux avec titre
        ScrollPane leftScrollPane = new ScrollPane(leftPanel);
        leftScrollPane.setFitToWidth(true);
        leftContainerPanel.setCenter(leftScrollPane);

        ScrollPane rightScrollPane = new ScrollPane(rightPanel);
        rightScrollPane.setFitToWidth(true);
        rightContainerPanel.setCenter(rightScrollPane);

        // Créer un SplitPane pour contenir les deux panneaux
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftContainerPanel, rightContainerPanel);
        splitPane.setDividerPositions(0.5); // Position initiale du diviseur à 50%

        // Ajouter le SplitPane à la fenêtre principale
        setCenter(splitPane);

        // Création du bouton "Retour" et ajout à la partie inférieure
        backButton = new Button("Retour");
        backButton.setOnAction(e -> onBackButtonClick());

        HBox bottomPanel = new HBox(); // Panneau pour contenir le bouton retour
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getChildren().add(backButton);

        // Ajouter le panneau de retour en bas de la fenêtre
        setBottom(bottomPanel);

        // Remplir les listes avec les utilisateurs
        updateUserLists();
    }

    // Méthode pour créer un panneau avec titre
    private BorderPane createTitledPanel(String title) {
        BorderPane panel = new BorderPane();
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setPadding(new Insets(5));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);
        panel.setTop(titleLabel);
        return panel;
    }

    // Méthode pour gérer l'événement du bouton "Retour"
    private void onBackButtonClick() {
        this.listListener.back();
    }

    // Met à jour les listes des utilisateurs dans les panneaux
    public void updateUserLists() {
        Platform.runLater(() -> {
            leftPanel.getChildren().clear();
            rightPanel.getChildren().clear();

            // Récupérer les utilisateurs non suivis
            List<User> allUsers = modelData.getAllUserNotfollowed();

            for (User user : allUsers) {
                HBox userPanel = createUserPanel(user, "Follow");
                leftPanel.getChildren().add(userPanel);
                // Ajouter un séparateur entre chaque utilisateur
                leftPanel.getChildren().add(createSeparator());
            }

            // Récupérer les utilisateurs suivis
            List<User> followedUsers = modelData.getFollowed();

            for (User user : followedUsers) {
                HBox userPanel = createUserPanel(user, "Unfollow");
                rightPanel.getChildren().add(userPanel);
                // Ajouter un séparateur entre chaque utilisateur
                rightPanel.getChildren().add(createSeparator());
            }
        });
    }

    // Créer un séparateur horizontal
    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setMaxWidth(Double.MAX_VALUE);
        return separator;
    }

    // Crée un panneau pour afficher un utilisateur avec son avatar et son tag
    private HBox createUserPanel(User user, String buttonLabel) {
        HBox userPanel = new HBox(10); // Espacement de 10px entre les éléments
        userPanel.setAlignment(Pos.CENTER_LEFT);
        userPanel.setPadding(new Insets(5));

        // Charger l'avatar
        ImageView avatarView = new ImageView();
        try {
            Image avatarImage = new Image(new File(user.getAvatarPath()).toURI().toString());
            avatarView.setImage(avatarImage);
            avatarView.setFitWidth(40);
            avatarView.setFitHeight(40);
            avatarView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créer le tag de l'utilisateur
        Label userTagLabel = new Label("@" + user.getUserTag());
        userTagLabel.setPadding(new Insets(0, 10, 0, 10));
        HBox.setHgrow(userTagLabel, Priority.ALWAYS); // Fait prendre tout l'espace disponible

        // Créer le bouton d'abonnement/désabonnement
        Button actionButton = new Button(buttonLabel);
        actionButton.setOnAction(e -> {
            if (buttonLabel.equals("Follow")) {
                this.listListener.follow(user);
            } else {
                this.listListener.unFollow(user);
            }
        });

        // Ajouter les composants au panneau
        userPanel.getChildren().addAll(avatarView, userTagLabel, actionButton);

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