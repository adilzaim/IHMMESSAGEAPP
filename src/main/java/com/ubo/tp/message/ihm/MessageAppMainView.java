package com.ubo.tp.message.ihm;

import com.ubo.tp.message.ihm.ListUserComponent.MainView;
import com.ubo.tp.message.ihm.messageComponent.MessagePanel;
import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import com.ubo.tp.message.ihm.listener.ExitListener;
import com.ubo.tp.message.ihm.userComponent.UserMapView;


import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainView extends Stage {

    private String selectedDirectory; // Stocke le répertoire sélectionné
    private ExitListener exitListener;
    private LoginView component;
    private SplitPane mainSplitPane;
    private SplitPane bottomSplitPane;
    private BorderPane loginContainer;
    private MessagePanel messagePanel;
    private BorderPane rightBottomContainer;

    /**
     * Définit l'écouteur de sortie de l'application.
     */
    public void setListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }

    /**
     * Définit le composant de login.
     */
    public void setComponent(LoginView component) {
        this.component = component;
    }

    /**
     * Initialise le composant de login.
     */
    private void initializeLoginComponent() {
        loginContainer.setCenter(component);
    }

    /**
     * Constructeur principal.
     */
    public MessageAppMainView(String directory, LoginView loginView) {
        super();
        setTitle("MessageApp - " + directory);

        this.component = loginView;

        // Chargement et définition de l'icône de l'application
        setApplicationIcon();

        // Configuration de la fenêtre
        setWidth(800);
        setHeight(600);
        setOnCloseRequest(event -> {
            if (exitListener != null) {
                exitListener.onExit();
            }
        });
        centerOnScreen(); // Centre la fenêtre

        // Initialisation du conteneur de login
        loginContainer = new BorderPane();

        // Initialisation du conteneur bottom-right
        rightBottomContainer = new BorderPane();

        // Initialisation du split pane horizontal pour le bas
        bottomSplitPane = new SplitPane();
        bottomSplitPane.getItems().add(rightBottomContainer);
        bottomSplitPane.setDividerPositions(0.7);  // Message panel prendra 70% de la largeur

        // Initialisation du split pane vertical principal
        mainSplitPane = new SplitPane();
        mainSplitPane.setOrientation(javafx.geometry.Orientation.VERTICAL);
        mainSplitPane.getItems().addAll(loginContainer, bottomSplitPane);
        mainSplitPane.setDividerPositions(0.8); // La partie supérieure occupe 80% de l'espace

        // Créer la scène avec le menu
        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(mainSplitPane);

        Scene scene = new Scene(root);
        setScene(scene);

        // Initialiser le composant de login
        initializeLoginComponent();
    }

    /**
     * Définit l'icône de l'application.
     */
    private void setApplicationIcon() {
        File iconFile = new File("src/main/resources/images/logo_20.png");
        if (iconFile.exists()) {
            try {
                Image icon = new Image(iconFile.toURI().toString());
                getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("⚠ Erreur : Impossible de charger l'icône de l'application.");
            }
        } else {
            System.err.println("⚠ Erreur : Fichier d'icône introuvable.");
        }
    }

    /**
     * Crée la barre de menu de l'application.
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menu "Fichier"
        Menu fileMenu = new Menu("Fichier");
        MenuItem exitItem = new MenuItem("Quitter");

        // Ajouter une icône au menu quitter si possible
        File iconFile = new File("src/main/resources/images/exitIcon_20.png");
        if (iconFile.exists()) {
            try {
                ImageView imageView = new ImageView(new Image(iconFile.toURI().toString()));
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
                exitItem.setGraphic(imageView);
            } catch (Exception e) {
                System.err.println("⚠ Erreur : Impossible de charger l'icône de quitter.");
            }
        }

        // Modification de l'action de quitter
        exitItem.setOnAction(e -> {
            if (exitListener != null) {
                exitListener.onExit();
            }
        });
        fileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(fileMenu);

        // Menu "Aide"
        Menu helpMenu = new Menu("?");
        MenuItem aboutItem = new MenuItem("À propos");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().add(helpMenu);

        return menuBar;
    }

    /**
     * Affiche la boîte de dialogue "À propos".
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText(null);

        // Créer un label avec le texte centré
        Label label = new Label("UBO M2-TILL\nDépartement Informatique");
        label.setAlignment(Pos.CENTER);

        // Ajouter une icône si possible
        File iconFile = new File("src/main/resources/images/logo_50.png");
        if (iconFile.exists()) {
            try {
                ImageView imageView = new ImageView(new Image(iconFile.toURI().toString()));
                VBox content = new VBox(10, imageView, label);
                content.setAlignment(Pos.CENTER);
                alert.getDialogPane().setContent(content);
            } catch (Exception e) {
                alert.setContentText("UBO M2-TILL\nDépartement Informatique");
            }
        } else {
            alert.setContentText("UBO M2-TILL\nDépartement Informatique");
        }

        alert.showAndWait();
    }

    /**
     * Ouvre un sélecteur de fichier pour choisir un répertoire.
     */
    private void chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionnez un répertoire d'échange");

        File selectedDir = chooser.showDialog(this);
        if (selectedDir != null) {
            selectedDirectory = selectedDir.getAbsolutePath();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Répertoire sélectionné : " + selectedDirectory);
            alert.showAndWait();

            setTitle("MessageApp - " + selectedDirectory);
        }
    }

    /**
     * Affiche la structure des dossiers du répertoire sélectionné.
     */
    private void showDirectoryStructure() {
        if (selectedDirectory == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Aucun répertoire sélectionné.");
            alert.showAndWait();
            return;
        }

        try {
            Path currentDir = Paths.get(selectedDirectory);
            String structure = Files.walk(currentDir, 2) // Limite la profondeur à 2
                    .map(path -> formatPath(currentDir, path))
                    .collect(Collectors.joining("\n"));

            TextArea textArea = new TextArea(structure);
            textArea.setEditable(false);
            textArea.setPrefRowCount(20);
            textArea.setPrefColumnCount(50);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Structure du répertoire sélectionné");
            dialog.getDialogPane().setContent(textArea);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la lecture du répertoire");
            alert.showAndWait();
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
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionnez un répertoire de travail");

        File selectedDir = chooser.showDialog(null); // `null` peut être utilisé mais pas recommandé, voir explication plus bas
        return (selectedDir != null) ? selectedDir.getAbsolutePath() : null;
    }


    /**
     * Revient à la vue de login.
     */
    public void setLoginView() {
        // Revenir à la vue de login
        loginContainer.setCenter(component);

        // Supprimer le panel de messages s'il existe
        if (messagePanel != null && bottomSplitPane.getItems().contains(messagePanel)) {
            bottomSplitPane.getItems().remove(messagePanel);
        }

        // Supprimer le composant de droite s'il existe
        rightBottomContainer.setCenter(null);
    }

    /**
     * Définit la vue de la carte des utilisateurs.
     */
    public void setUserMapView(UserMapView userMapView) {
        // Remplacer le contenu de la partie supérieure
        loginContainer.setCenter(userMapView);
    }

    /**
     * Définit la vue des messages.
     */
    public void setMessageView(MessagePanel messagePanel) {
        // Stocker la référence du panel de messages
        this.messagePanel = messagePanel;

        // S'assurer que le bottomSplitPane a au plus 2 éléments
        if (bottomSplitPane.getItems().size() >= 2) {
            bottomSplitPane.getItems().set(0, messagePanel);
        } else {
            bottomSplitPane.getItems().add(0, messagePanel);
        }

        // Ajuster la position du diviseur pour montrer le panel de messages
        bottomSplitPane.setDividerPositions(0.7);
    }

    /**
     * Ajoute un composant à la zone bottom-right.
     */
    public void setRightBottomComponent(Pane component) {
        rightBottomContainer.setCenter(component);
    }

    public void setUserListView(MainView mainView) {
        // Ajouter la vue des utilisateurs dans la partie supérieure
        loginContainer.setCenter(mainView);
    }

    public void setSearchUser(SearchUserView searchUserView) {
        // Ajouter la vue des utilisateurs dans la partie supérieure
        loginContainer.setCenter(searchUserView);
    }
}