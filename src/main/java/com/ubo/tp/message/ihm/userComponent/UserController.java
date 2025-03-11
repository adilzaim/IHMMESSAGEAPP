package main.java.com.ubo.tp.message.ihm.userComponent;

import com.ubo.tp.message.ihm.searchUser.SearchUserView;
import main.java.com.ubo.tp.message.ihm.ListUserComponent.MainView;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;

public class UserController {
    private UserModel userModel;
    private UserMapView userView;

    private UserMapViewListener listener;
    private MessageAppMainView mMainView;

    private MainView mainView;

    public UserController(UserModel userModel, MessageAppMainView mMainView, UserMapView userView, MainView mainView) {

        this.userModel = userModel;
        this.userView = userView;
        this.mMainView = mMainView;
        this.mMainView.setUserMapView(userView );
        this.initialiseListener();
        this.mainView = mainView;


    }

    public void initialiseListener(){
        this.listener = new UserMapViewListener() {
            @Override
            public void onUserProfileView() {

            }

            @Override
            public void onUserListView() {

                mMainView.setUserListView(mainView);

            }

            @Override
            public void onUserSearch(SearchUserView searchUserView) {
                mMainView.setSearchUser(searchUserView);

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
