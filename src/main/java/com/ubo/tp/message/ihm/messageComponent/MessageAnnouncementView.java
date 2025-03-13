package com.ubo.tp.message.ihm.messageComponent;

import com.ubo.tp.message.datamodel.Message;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAnnouncementView extends BorderPane {
    private TextFlow messageFlow;
    private ScrollPane scrollPane;

    private List<Message> originalMessageList;
    private List<Message> filteredMessageList;

    private TextField searchField;
    private Button searchButton, resetButton;

    private FilterListener filterListener;

    public MessageAnnouncementView(List<Message> messageList) {
        this.originalMessageList = messageList;
        this.filteredMessageList = new ArrayList<>(messageList);

        // Création du TextFlow pour afficher les messages
        messageFlow = new TextFlow();
        messageFlow.setPadding(new Insets(10));
        messageFlow.setLineSpacing(5);

        // Ajout du TextFlow à un ScrollPane
        scrollPane = new ScrollPane(messageFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        setCenter(scrollPane);

        // Ajout de la barre de recherche
        addSearchBar();

        // Affichage des messages
        displayMessages();
    }

    private void addSearchBar() {
        searchField = new TextField();
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-border-color: #2196F3; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 5 10 5 10;");

        searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
            if (filterListener != null) {
                filterListener.onFilter(searchField.getText().trim());
            }
        });

        resetButton = new Button("Réinitialiser");
        resetButton.setOnAction(e -> {
            searchField.setText(""); // Efface la barre de recherche
            if(filterListener != null) {
                filterListener.onFilterRemove();
            }
        });

        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        searchContainer.setPadding(new Insets(10));
        searchContainer.getChildren().addAll(searchField, searchButton, resetButton);

        setTop(searchContainer);
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public void updateFilteredMessages(List<Message> newFilteredMessages) {
        this.filteredMessageList = newFilteredMessages;
        repaintMessages();
    }

    public void unsetMessageList(List<Message> newMessageList) {
        this.originalMessageList = newMessageList;
        this.filteredMessageList = new ArrayList<>(newMessageList);
        repaintMessages();
    }

    private void repaintMessages() {
        Platform.runLater(() -> {
            clearMessages();
            displayMessages();
        });
    }

    private void displayMessages() {
        for (Message msg : filteredMessageList) {
            // Formatage de la date
            Date date = new Date(msg.getEmissionDate());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String timestamp = sdf.format(date);

            // Création des éléments de texte stylisés
            Text timestampText = new Text(timestamp + " ");
            timestampText.setFill(Color.GRAY);
            timestampText.setFont(Font.font("Arial", FontPosture.ITALIC, 14));

            Text tagText = new Text("@" + msg.getSender().getUserTag() + " ");
            tagText.setFill(Color.web("#2196F3"));
            tagText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Text messageText = new Text(msg.getText() + "\n");
            messageText.setFill(Color.BLACK);
            messageText.setFont(Font.font("Arial", 14));

            // Ajout des éléments au TextFlow
            messageFlow.getChildren().addAll(timestampText, tagText, messageText);
        }

        // Scroll automatique vers le bas
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private void clearMessages() {
        messageFlow.getChildren().clear();
    }
}