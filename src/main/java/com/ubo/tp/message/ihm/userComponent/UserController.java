package main.java.com.ubo.tp.message.ihm.userComponent;

import main.java.com.ubo.tp.message.ihm.MessageAppMainView;
import main.java.com.ubo.tp.message.ihm.UserMapView;

public class UserController {
    private UserModel userModel;
    private UserMapView userView;

    public UserController(UserModel userModel, MessageAppMainView mMainView, UserMapView userView) {
        this.userModel = userModel;
        this.userView = userView;
        mMainView.setUserMapView(userView);

    }


}
