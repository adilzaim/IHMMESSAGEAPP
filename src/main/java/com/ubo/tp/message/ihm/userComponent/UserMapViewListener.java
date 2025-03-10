package main.java.com.ubo.tp.message.ihm.userComponent;

import main.java.com.ubo.tp.message.ihm.ListUserComponent.MainView;

public interface UserMapViewListener {

    void onUserProfileView();
    void onUserListView();
    void onUserSearch();
    void onUserFollow(String userTag);
    void onUserUnfollow(String userTag);
    void onLogout();
}
