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

    public boolean doLogin(String username, String tag) {
        for (User i : this.mDatabase.getUsers()) {
            if (i.getName().equals(username) && i.getUserTag().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public void createUser(String nom, String tag, String avatarPath){
        this.mDatabase.addUser(new User(UUID.randomUUID(), tag,"hhhh", nom,new HashSet<>() ,avatarPath));

    }


}
