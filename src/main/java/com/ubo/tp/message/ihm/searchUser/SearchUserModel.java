package com.ubo.tp.message.ihm.searchUser;

import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import java.util.ArrayList;
import java.util.List;
import com.ubo.tp.message.core.database.IDatabase;
public class SearchUserModel implements IDatabaseObserver {
    private List<User> users;
    private List<SearchModelObserver> observers;
    private IDatabase db;

    public SearchUserModel( IDatabase db ) {
        this.users = new ArrayList<>();

        this.observers = new ArrayList<>();
        this.db = db;
        this.initializeUsers();
    }

    // Méthode pour ajouter un observateur
    public void addObserver(SearchModelObserver observer) {
        observers.add(observer);
    }


    // Notifier tous les observateurs des changements
    private void notifyObservers() {
        for (SearchModelObserver observer : observers) {
            observer.onSearchResultsUpdated(users);
        }
    }

    // Méthode pour initialiser les données utilisateurs (simulée ici)
    public void initializeUsers() {

        for(User user : this.db.getUsers()){
            this.users.add(user);
        }

    }

    public void setUsers(List<User> list){
        this.users = list;
        this.notifyObservers();
    }

    public List<User> getUsersModel() {
        return this.users;
    }

    @Override
    public void notifyMessageAdded(Message addedMessage) {

    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {

    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {

    }

    @Override
    public void notifyUserAdded(User addedUser) {
        List<User> list = new ArrayList<>();
        for(User user : this.db.getUsers()){
            list.add(user);
        }
        this.setUsers(list);
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {

    }

    @Override
    public void notifyUserModified(User modifiedUser) {

    }
}
