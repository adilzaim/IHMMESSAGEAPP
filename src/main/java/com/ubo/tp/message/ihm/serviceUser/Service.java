package main.java.com.ubo.tp.message.ihm.serviceUser;

import main.java.com.ubo.tp.message.core.EntityManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.MessageApp;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Service {
    protected IDatabase mDatabase;
    protected EntityManager mEntityManager;

    public Service(IDatabase db , EntityManager entityManager){
        this.mDatabase = db;
        this.mEntityManager = entityManager;
    }

    public User doLogin(String username, String tag, UserModel userModel) {
        for (User i : this.mDatabase.getUsers()) {
            if (i.getName().equals(username) && i.getUserTag().equals(tag)) {
                userModel.setUser(i);
                return i;
            }
        }
        return null;
    }

    public boolean createUser(String nom, String tag, String avatarPath, String password) {
        // Vérifier si l'utilisateur existe déjà
        boolean exists = this.mDatabase.getUsers().stream().anyMatch(i -> i.getUserTag().equals(tag));

        // Si l'utilisateur n'existe pas, l'ajouter
        if (!exists) {
            this.mDatabase.addUser(new User(UUID.randomUUID(), tag, password, nom, new HashSet<>(), avatarPath));
        }

        return !exists;
    }

    public List<Message> getMessageUser(User user) {
        List<Message> liste = new ArrayList<>();
        for(Message msg : this.mDatabase.getUserMessages(user)){
            liste.add(msg);
        }
        for(User u : this.mDatabase.getUsers()){
            if(user.isFollowing(u)) {
                for(Message msg : this.mDatabase.getUserMessages(u)){
                    liste.add(msg);

                }
            }
        }
        return liste;
    }


}
