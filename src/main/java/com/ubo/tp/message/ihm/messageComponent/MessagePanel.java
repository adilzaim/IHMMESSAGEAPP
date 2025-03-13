package com.ubo.tp.message.ihm.messageComponent;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.util.List;

import com.ubo.tp.message.datamodel.User;

public class MessagePanel extends BorderPane implements MessageModelObserver {
    private TextArea messageArea;
    private TextField inputField;
    private Button publishButton;
    private User currentUser;
    private MessageListener messageListener;
    private MessageModel model;

    public MessagePanel(User currentUser, MessageModel model) {
        this.model = model;
        this.model.addObserver(this);
        this.currentUser = currentUser;

        setPrefSize(500, 300); // Taille préférée pour le panneau

        // Zone d'affichage des messages
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setPrefSize(480, 200);
        // Assurer une bonne visibilité du texte
        messageArea.setStyle("-fx-control-inner-background: white;");
        messageArea.setStyle("-fx-text-fill: gray;");
        messageArea.setFont(Font.font("SansSerif", FontPosture.ITALIC, 14));

        // Ajout de la TextArea avec le scrolling
        ScrollPane scrollPane = new ScrollPane(messageArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(480, 200);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        setCenter(scrollPane);

        // Zone de saisie + bouton publier
        HBox inputPanel = new HBox();
        inputField = new TextField();
        inputField.setPrefWidth(400);
        publishButton = new Button("Publier");

        inputPanel.getChildren().addAll(inputField, publishButton);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setSpacing(10);

        setBottom(inputPanel);

        // Action du bouton Publier
        publishButton.setOnAction(e -> {
            String text = inputField.getText().trim();
            // Vérifie si le message dépasse 200 caractères
            if (text.length() > 200) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Message trop long");
                alert.setHeaderText(null);
                alert.setContentText("Le message ne doit pas dépasser 200 caractères.");
                alert.showAndWait();
                return; // Empêche l'envoi du message
            }
            if (!text.isEmpty() && messageListener != null) {
                messageListener.onMessageSend(currentUser.getUserTag(), text);
                inputField.setText("");
            }
        });

        // Initialisation des messages au démarrage
        initializeMessages();
    }

    // Méthode pour enregistrer un listener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    /**
     * Fonction d'initialisation qui charge les messages existants lors de la création du composant
     * Cette méthode est appelée une seule fois dans le constructeur
     */
    private void initializeMessages() {
        Platform.runLater(() -> {
            // Récupérer la liste initiale des messages depuis le modèle
            List<String> initialMessages = model.getCurrentList();

            // Vider la zone de texte
            messageArea.setText("");

            System.out.println("Initialisation des messages - Nombre: " +
                    (initialMessages != null ? initialMessages.size() : 0));

            // Afficher chaque message dans la zone de texte
            if (initialMessages != null && !initialMessages.isEmpty()) {
                for (String message : initialMessages) {
                    System.out.println("Initialisation - Ajout du message: " + message);
                    messageArea.appendText(message + "\n");
                }

                // Scroller vers le bas pour voir les derniers messages
                messageArea.positionCaret(messageArea.getText().length());
            }
        });
    }

    @Override
    public void onMessageListUpdated(List<String> updatedList) {
        // Utiliser Platform.runLater pour garantir que les mises à jour de l'IHM se font sur le thread JavaFX
        Platform.runLater(() -> {
            // Effacer d'abord le contenu actuel
            messageArea.setText("");

            // Ajouter chaque message comme une nouvelle ligne
            for (String message : updatedList) {
                System.out.println("Ajout du message: " + message); // Debug
                messageArea.appendText(message + "\n");
            }

            // Scroller vers le bas pour voir les derniers messages
            messageArea.positionCaret(messageArea.getText().length());
        });
    }

    public void showPopUp(String message) {
        // Make sure to run on JavaFX application thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showPopUp(message));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }
}