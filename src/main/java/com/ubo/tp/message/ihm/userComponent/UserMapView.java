package com.ubo.tp.message.ihm.userComponent;

import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import com.ubo.tp.message.datamodel.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class UserMapView extends BorderPane {

    // User information
    private User user;

    // Listener instance
    private UserMapViewListener listener;
    private SearchUserView searchUserView;

    /**
     * Constructor for UserMapView
     * @param user containing user information
     * @param searchUserView the search view
     */
    public UserMapView(User user, SearchUserView searchUserView) {
        this.user = user;
        this.searchUserView = searchUserView;

        // Create main panel with user information
        VBox userInfoPanel = createUserInfoPanel(user);
        setCenter(userInfoPanel);

        // Create action buttons panel
        HBox actionPanel = createActionPanel();
        setBottom(actionPanel);
    }

    /**
     * Creates panel with user information
     * @return VBox with user details
     */
    private VBox createUserInfoPanel(User user) {
        VBox panel = new VBox(10); // 10px spacing between elements
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.TOP_LEFT);

        // Avatar
        if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
            try {
                File file = new File(user.getAvatarPath());
                Image image = new Image(file.toURI().toString());

                // Create a circle for the avatar
                Circle circle = new Circle(40); // 40px radius = 80px diameter
                circle.setFill(new ImagePattern(image));

                panel.getChildren().add(circle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // User information
        panel.getChildren().add(createInfoLabel("UUID", user.getUuid().toString()));
        panel.getChildren().add(createInfoLabel("Tag", user.getUserTag()));
        panel.getChildren().add(createInfoLabel("Nom", user.getName()));
        panel.getChildren().add(createInfoLabel("Mots de passe", user.getUserPassword()));
       // panel.getChildren().add(createInfoLabel("Tags suivis", String.join(", ", user.getFollows())));

        return panel;
    }

    private Label createInfoLabel(String key, String value) {
        Label label = new Label(key + ": " + value);
        return label;
    }

    /**
     * Creates panel with action buttons
     * @return HBox with user action buttons
     */
    private HBox createActionPanel() {
        HBox panel = new HBox(10); // 10px spacing between buttons
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER);

        // Profile View Button
        Button profileButton = new Button("Voir Profil");
        profileButton.setOnAction(e -> {
            if (listener != null) {
                listener.onUserProfileView();
            }
        });
        panel.getChildren().add(profileButton);

        // User List Button
        Button userListButton = new Button("Liste Utilisateurs");
        userListButton.setOnAction(e -> {
            if (listener != null) {
                listener.onUserListView();
            }
        });
        panel.getChildren().add(userListButton);

        // Search Button
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
            if (listener != null) {
                listener.onUserSearch(searchUserView);
            }
        });
        panel.getChildren().add(searchButton);

        // Logout Button
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setOnAction(e -> {
            if (listener != null) {
                listener.onLogout();
            }
        });
        panel.getChildren().add(logoutButton);

        return panel;
    }

    /**
     * Sets the listener for user interactions
     * @param listener UserMapViewListener to handle user actions
     */
    public void setUserMapViewListener(UserMapViewListener listener) {
        this.listener = listener;
    }

    public void setListener(UserMapViewListener listener) {
        this.listener = listener;
    }
}