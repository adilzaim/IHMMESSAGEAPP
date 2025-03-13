package com.ubo.tp.message.ihm.searchUser;

import com.ubo.tp.message.datamodel.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.util.List;

/**
 * Vue simplifiée pour la recherche d'utilisateurs en JavaFX.
 * Contient uniquement un champ de recherche, une liste et trois boutons.
 */
public class SearchUserView extends BorderPane implements SearchModelObserver {
    private TextField searchField;
    private Button searchButton;
    private Button resetButton;
    private ListView<User> resultsList;
    private SearchListener searchListener;
    private SearchUserModel model;
    private Button backButton; // Déclaration du bouton de retour

    public SearchUserView(SearchUserModel model) {
        this.model = model;
        this.model.addObserver(this);

        // Panel supérieur contenant la barre de recherche et les boutons
        HBox searchPanel = new HBox(10); // 10px d'espacement
        searchPanel.setPadding(new Insets(10));

        searchField = new TextField();
        searchField.setPrefWidth(300);

        searchButton = new Button("Rechercher");
        resetButton = new Button("Réinitialiser");
        backButton = new Button("Retour");

        // Ajout des actions aux boutons
        searchButton.setOnAction(e -> {
            if (searchListener != null) {
                searchListener.onSearch(searchField.getText());
            }
        });

        resetButton.setOnAction(e -> {
            searchField.setText("");
            if (searchListener != null) {
                searchListener.reset();
            }
        });

        backButton.setOnAction(e -> searchListener.backTo());

        searchPanel.getChildren().addAll(searchField, searchButton, resetButton, backButton);

        // Liste des résultats
        resultsList = new ListView<>();
        resultsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Charger les utilisateurs initiaux
        updateUserList(model.getUsersModel());

        // Configuration du layout principal
        setTop(searchPanel);
        setCenter(resultsList);
        setPadding(new Insets(10));
    }

    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }

    @Override
    public void onSearchResultsUpdated(List<User> results) {
        System.out.println(results);
        updateUserList(results);
    }

    private void updateUserList(List<User> users) {
        ObservableList<User> observableUsers = FXCollections.observableArrayList();
        if (users != null) {
            observableUsers.addAll(users);
        }
        resultsList.setItems(observableUsers);
    }
}