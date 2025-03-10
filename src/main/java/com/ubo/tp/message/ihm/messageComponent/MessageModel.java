package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModelObserver;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class MessageModel {
    private List<Object> currentList;
    private List<MessageModelObserver> observers;

    public MessageModel() {
        this.currentList = new ArrayList<>();
        this.observers = new ArrayList<>();
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
    public void setMessageList(List<Object> newList) {
        this.currentList = new ArrayList<>(newList);
        notifyObservers();
    }

    // Ajouter un message à la liste et notifier
    public void addMessage(Object message) {
        this.currentList.add(message);
        notifyObservers();
    }

    // Supprimer un message de la liste et notifier
    public void removeMessage(Object message) {
        this.currentList.remove(message);
        notifyObservers();
    }

    // Récupérer la liste actuelle des messages
    public List<Object> getCurrentList() {
        return new ArrayList<>(currentList);
    }
}

