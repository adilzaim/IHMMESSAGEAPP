package main.java.com.ubo.tp.message.ihm.serviceUser;

import main.java.com.ubo.tp.message.core.EntityManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.HashSet;
import java.util.UUID;

public class Service {
    protected IDatabase mDatabase;
    protected EntityManager mEntityManager;

    public Service(IDatabase db , EntityManager entityManager){
        this.mDatabase = db;
        this.mEntityManager = entityManager;
    }

    public User doLogin(String username, String tag) {
        for (User i : this.mDatabase.getUsers()) {
            if (i.getName().equals(username) && i.getUserTag().equals(tag)) {
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


}
