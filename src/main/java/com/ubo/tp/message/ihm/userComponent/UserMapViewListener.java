package com.ubo.tp.message.ihm.userComponent;

import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import com.ubo.tp.message.ihm.searchUser.SearchUserView;

public interface UserMapViewListener {

    void onUserProfileView();
    void onUserListView();
    void onUserSearch(SearchUserView searchUserView);
    void onUserFollow(String userTag);
    void onUserUnfollow(String userTag);
    void onLogout();
}
