package main.java.com.ubo.tp.message.ihm.userComponent;

public interface UserMapViewListener {

    void onUserProfileView();
    void onUserListView();
    void onUserSearch();
    void onUserFollow(String userTag);
    void onUserUnfollow(String userTag);
    void onLogout();
}
