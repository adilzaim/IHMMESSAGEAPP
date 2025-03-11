package com.ubo.tp.message.ihm.searchUser;

import main.java.com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Vue simplifiée pour la recherche d'utilisateurs.
 * Contient uniquement un champ de recherche, une liste et deux boutons.
 */
public class SearchUserView extends JPanel implements SearchModelObserver {
    private JTextField searchField;
    private JButton searchButton;
    private JButton resetButton;
    private JList<User> resultsList;
    private SearchListener searchListener;
    private SearchUserModel model;
    private JButton backButton; // Déclaration du bouton de retour


    public SearchUserView(SearchUserModel model) {
        this.model = model;
        this.model.addObserver(this);
        setLayout(new BorderLayout());

        // Panel supérieur contenant la barre de recherche et les boutons
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchButton = new JButton("Rechercher");
        resetButton = new JButton("Réinitialiser");
        // Création du bouton "Retour" et ajout à la partie inférieure
        backButton = new JButton("Retour");
        backButton.addActionListener(e -> searchListener.backTo());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);


        // Liste des résultats
        resultsList = new JList<>(new DefaultListModel<>());
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Charger les utilisateurs initiaux
        updateUserList(model.getUsersModel());

        JScrollPane scrollPane = new JScrollPane(resultsList);

        // Ajouter les composants au panel principal
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Actions des boutons
        searchButton.addActionListener(e -> {
            if (searchListener != null) {
                searchListener.onSearch(searchField.getText());
            }
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            if (searchListener != null) {
                searchListener.reset();
            }
        });
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
        DefaultListModel<User> listModel = new DefaultListModel<>();
        if (users != null) {
            users.forEach(listModel::addElement);
        }
        resultsList.setModel(listModel);
    }
}