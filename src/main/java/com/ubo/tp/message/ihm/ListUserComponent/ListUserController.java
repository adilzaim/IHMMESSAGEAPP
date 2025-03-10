package main.java.com.ubo.tp.message.ihm.ListUserComponent;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;
import main.java.com.ubo.tp.message.ihm.messageComponent.*;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModel;

public class ListUserController {
    private UserModel userModel;
    private MainView mainView;
    private ListListener listListener;

    private ModelData modelData;

    private ListService service;

    private MessageAppMainView mMain;

    public ListUserController(UserModel userModel , MainView mainView , ModelData modelData, ListService service){

        this.userModel= userModel;
        this.mainView = mainView;
        this.modelData = modelData;
        this.service = service;

    }




}
