package main.java.com.ubo.tp.message.ihm.userComponent;

import main.java.com.ubo.tp.message.datamodel.User;

public interface UserModelObserver {
    void onUserLoggedIn(User user);
    void onUserLoggedOut();
}
