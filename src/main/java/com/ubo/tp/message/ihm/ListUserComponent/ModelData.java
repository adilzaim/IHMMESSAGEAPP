package main.java.com.ubo.tp.message.ihm.ListUserComponent;

import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.List;
import java.util.ArrayList;

public class ModelData implements IDatabaseObserver {
    private List<User> allUserNotfollowed;
    private List<User> followed;
    private List<ModelObserver> observers;  // Liste des observateurs
    private IDatabase db;

    // Constructeur pour initialiser les listes et les observateurs
    public ModelData(IDatabase db) {
        this.db =db;

        this.allUserNotfollowed = new ArrayList<>();
        this.followed = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.db.addObserver(this);
    }

    // Ajouter un observateur à la liste
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    // Retirer un observateur de la liste
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    // Notifier tous les observateurs d'un changement
    private void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.update();
        }
    }

    // Getter pour allUserNotfollowed
    public List<User> getAllUserNotfollowed() {
        return allUserNotfollowed;
    }

    // Setter pour allUserNotfollowed
    public void setAllUserNotfollowed(List<User> allUserNotfollowed) {
        this.allUserNotfollowed = allUserNotfollowed;
        notifyObservers();  // Notifier les observateurs après un changement
    }

    // Getter pour followed
    public List<User> getFollowed() {
        return followed;
    }

    // Setter pour followed
    public void setFollowed(List<User> followed) {
        this.followed = followed;
        notifyObservers();  // Notifier les observateurs après un changement
    }

    // Méthode pour ajouter un utilisateur à la liste des utilisateurs non suivis
    public void addUserNotFollowed(User user) {
        if (!allUserNotfollowed.contains(user)) {
            allUserNotfollowed.add(user);
            notifyObservers();  // Notifier les observateurs après un changement
        }
    }

    // Méthode pour retirer un utilisateur de la liste des utilisateurs non suivis
    public void removeUserNotFollowed(User user) {
        allUserNotfollowed.remove(user);
        notifyObservers();  // Notifier les observateurs après un changement
    }

    // Méthode pour ajouter un utilisateur à la liste des utilisateurs suivis
    public void followUser(User user) {
        if (!followed.contains(user)) {
            followed.add(user);
            removeUserNotFollowed(user);  // Retirer de la liste des non suivis
            notifyObservers();  // Notifier les observateurs après un changement
        }
    }




    // Méthode pour récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(allUserNotfollowed);
        allUsers.addAll(followed);
        return allUsers;
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
        if(this.allUserNotfollowed != null)
        this.allUserNotfollowed.add(addedUser);
        else{
            this.allUserNotfollowed = new ArrayList<>();
            this.allUserNotfollowed.add(addedUser);
        }
        this.setAllUserNotfollowed( this.allUserNotfollowed);
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {

    }

    @Override
    public void notifyUserModified(User modifiedUser) {

    }
}
