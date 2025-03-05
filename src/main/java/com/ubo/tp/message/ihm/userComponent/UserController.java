package main.java.com.ubo.tp.message.ihm.userComponent;

import main.java.com.ubo.tp.message.ihm.MessageAppMainView;

public class UserController {
    private UserModel userModel;
    private UserMapView userView;

    private UserMapViewListener listener;

    public UserController(UserModel userModel, MessageAppMainView mMainView, UserMapView userView) {

        this.userModel = userModel;
        this.userView = userView;
        mMainView.setUserMapView(userView );
        this.initialiseListener();


    }

    public void initialiseListener(){
        this.listener = new UserMapViewListener() {
            @Override
            public void onUserProfileView() {

            }

            @Override
            public void onUserListView() {

            }

            @Override
            public void onUserSearch() {

            }

            @Override
            public void onUserFollow(String userTag) {

            }

            @Override
            public void onUserUnfollow(String userTag) {

            }

            @Override
            public void onLogout() {
                userModel.logout();
            }
        };

        this.userView.setListener(this.listener);
    }


}
