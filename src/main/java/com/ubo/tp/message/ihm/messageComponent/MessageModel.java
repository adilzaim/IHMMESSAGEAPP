package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModelObserver;

import java.text.SimpleDateFormat;
import java.util.*;

import java.util.ArrayList;
import java.util.List;

public class MessageModel implements IDatabaseObserver {
    private List<String> currentList;
    private List<MessageModelObserver> observers;

    private IDatabase db ;

    private User currentUser;
    public MessageModel(IDatabase db , User currentUser) {
        this.currentList = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.currentUser = currentUser;
        this.db = db;
        this.db.addObserver(this);
    }

    // Ajouter un observer
    public void addObserver(MessageModelObserver observer) {
        observers.add(observer);
    }

    // Supprimer un observer
    public void removeObserver(MessageModelObserver observer) {
        observers.remove(observer);
    }

    // Notifier tous les observers d'un changement
    private void notifyObservers() {
        for (MessageModelObserver observer : observers) {
            observer.onMessageListUpdated(currentList);
        }
    }

    // Mettre à jour la liste des messages et notifier les observers
    public void setMessageList(List<String> newList) {
        this.currentList = new ArrayList<>(newList);
        notifyObservers();
    }

    // Ajouter un message à la liste et notifier
    public void addMessage(String message) {
        this.currentList.add(message);
        notifyObservers();
    }

    // Supprimer un message de la liste et notifier
    public void removeMessage(Object message) {
        this.currentList.remove(message);
        notifyObservers();
    }

    // Récupérer la liste actuelle des messages
    public List<String> getCurrentList() {
        return new ArrayList<>(currentList);
    }

    @Override
    public void notifyMessageAdded(Message addedMessage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
        Date messageDate = new Date(addedMessage.getEmissionDate());
        if(this.currentUser.isFollowing(addedMessage.getSender())) {
        // Convertir le timestamp (long) en date formatée

            String formattedDate = dateFormat.format(messageDate);

        // Message amélioré avec espace après le tag et date formatée
            String msg = "L'utilisateur " + addedMessage.getSender().getUserTag() + " a envoyé un message le " + formattedDate;            this.addMessage(msg);
        }

    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {

    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {

    }

    @Override
    public void notifyUserAdded(User addedUser) {

    }

    @Override
    public void notifyUserDeleted(User deletedUser) {

    }

    @Override
    public void notifyUserModified(User modifiedUser) {

    }
}

