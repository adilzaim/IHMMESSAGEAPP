package com.ubo.tp.message.ihm.ListUserComponent;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.core.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class ListService {

    private IDatabase db;


    public ListService(IDatabase db) {
        this.db = db;
    }

    // Méthode pour initialiser les listes lors de la connexion
    public void initializeFollowLists(User currentUser, ModelData modelData) {
        List<User> listFollow = new ArrayList<>();
        List<User> listUnfollow = new ArrayList<>();

        // Parcourir tous les utilisateurs dans la base de données
        for (User user : this.db.getUsers()) {
            // Ne pas inclure l'utilisateur courant dans les listes
            if (user.equals(currentUser)) {
                continue;
            }

            // Si l'utilisateur courant suit cet utilisateur, l'ajouter à listFollow
            if (currentUser.getFollows().contains(user.getUserTag())) {
                listFollow.add(user);
            } else {
                // Sinon, l'ajouter à listUnfollow
                listUnfollow.add(user);
            }
        }

        // Mettre à jour les listes dans modelData
        modelData.setFollowed(listFollow);
        modelData.setAllUserNotfollowed(listUnfollow);
    }

    public void follow(User currentUser, User userToFollow, ModelData modelData, EntityManager entityManager) {
        if (userToFollow.equals(currentUser)) return;
        if (!currentUser.isFollowing(userToFollow)){
            currentUser.addFollowing(userToFollow.getUserTag());
            entityManager.writeUserFile(currentUser);
        }


    }

    public void unfollow(User currentUser, User userToUnFollow, ModelData modelData, EntityManager entityManager) {
        currentUser.removeFollowing(userToUnFollow.getUserTag());
        entityManager.writeUserFile(currentUser);

    }

    // Méthode utilitaire pour mettre à jour les listes
    public void updateFollowLists(User currentUser, ModelData modelData) {
        List<User> listFollow = new ArrayList<>();
        List<User> listUnfollow = new ArrayList<>();

        for (User user : this.db.getUsers()) {
            // Ne pas inclure l'utilisateur courant dans les listes
            if (user.equals(currentUser)) {
                continue;
            }

            if (currentUser.getFollows().contains(user.getUserTag())) {
                listFollow.add(user);
            } else {
                listUnfollow.add(user);
            }
        }

        modelData.setFollowed(listFollow);
        modelData.setAllUserNotfollowed(listUnfollow);
    }
}