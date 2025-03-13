package com.ubo.tp.message.ihm;

import com.ubo.tp.message.ihm.listener.LoginListener;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginView extends GridPane {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button createAccountButton;
    private Map<String, String> userCredentials;
    private LoginListener loginListener;

    public LoginView() {
        // Initialize user credentials storage
        userCredentials = new HashMap<>();

        // Set up the layout
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.CENTER);

        // Username label and field
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");

        // Password (tag) label and field
        Label passwordLabel = new Label("Tag de l'utilisateur:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre tag");

        // Login button
        loginButton = new Button("Connexion");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        // Create Account button
        createAccountButton = new Button("Créer un compte");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);

        // Add components to the grid
        add(usernameLabel, 0, 0);
        add(usernameField, 1, 0);
        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);
        add(loginButton, 0, 2, 2, 1);
        add(createAccountButton, 0, 3, 2, 1);

        // Add action listeners
        setupListeners();
    }

    private void setupListeners() {
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String tag = passwordField.getText();

            if (loginListener != null) {
                loginListener.loginVerify(username, tag);
            }
        });

        createAccountButton.setOnAction(e -> {
            showCreateAccountDialog();
        });
    }

    private void showCreateAccountDialog() {
        // Create a dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Créer un compte");

        // Set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create the grid and set constraints
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create form fields
        TextField newUsernameField = new TextField();
        newUsernameField.setPromptText("Nom d'utilisateur");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Mot de passe");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmer le mot de passe");

        TextField tagField = new TextField();
        tagField.setPromptText("Tag");

        TextField avatarPath = new TextField();
        avatarPath.setPromptText("Chemin de l'avatar");
        avatarPath.setEditable(false);

        Button browseButton = new Button("Parcourir");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un avatar");

            // Add filters for image files
            FileChooser.ExtensionFilter imageFilter =
                    new FileChooser.ExtensionFilter("Images (PNG, JPEG)", "*.png", "*.jpg", "*.jpeg", "*.jpe");
            fileChooser.getExtensionFilters().add(imageFilter);

            // Show the file dialog
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                avatarPath.setText(selectedFile.getAbsolutePath());
            }
        });

        // Layout for the avatar selection (text field + browse button)
        HBox avatarBox = new HBox(10);
        avatarBox.getChildren().addAll(avatarPath, browseButton);
        HBox.setHgrow(avatarPath, Priority.ALWAYS);

        // Add labels and fields to the grid
        grid.add(new Label("Nom d'utilisateur:"), 0, 0);
        grid.add(newUsernameField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(newPasswordField, 1, 1);
        grid.add(new Label("Confirmer le mot de passe:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(new Label("Tag:"), 0, 3);
        grid.add(tagField, 1, 3);
        grid.add(new Label("Avatar (png, jpeg):"), 0, 4);
        grid.add(avatarBox, 1, 4);

        // Set the dialog content
        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default
        Platform.runLater(() -> newUsernameField.requestFocus());

        // Process the result
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == okButtonType) {
            String newUsername = newUsernameField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String tagString = tagField.getText();
            String avatarPathString = avatarPath.getText();

            // Validate input
            if (newUsername.isEmpty() || newPassword.isEmpty() || tagString.isEmpty()) {
                showPopup("Nom utilisateur, mot de passe et tag sont obligatoires", false);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showPopup("Les mots de passe ne correspondent pas", false);
                return;
            }

            // Add new user
            if (this.loginListener != null) {
                this.loginListener.createUser(newUsername, tagString, avatarPathString, newPassword);
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Simple authentication - in a real app, you'd use more secure methods
        return userCredentials.containsKey(username) &&
                userCredentials.get(username).equals(password);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    // Popup utility
    public static void showPopup(String message, boolean isNormal) {
        Alert alert;
        if (isNormal) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
        }

        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}