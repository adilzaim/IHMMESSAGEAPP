package com.ubo.tp.message.ihm.userComponent;

import com.ubo.tp.message.datamodel.User;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private User currentUser;
    private List<UserModelObserver> observers = new ArrayList<>();

    public void setUser(User user) {
        this.currentUser = user;
        notifyUserLoggedIn();
    }

    public void logout() {
        this.currentUser = null;
        notifyUserLoggedOut();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addObserver(UserModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(UserModelObserver observer) {
        observers.remove(observer);
    }

    public void notifyUserLoggedIn() {
        for (UserModelObserver observer : observers) {
            observer.onUserLoggedIn(currentUser);
        }
    }

    public void notifyUserLoggedOut() {
        for (UserModelObserver observer : observers) {
            observer.onUserLoggedOut();
        }
    }
}
