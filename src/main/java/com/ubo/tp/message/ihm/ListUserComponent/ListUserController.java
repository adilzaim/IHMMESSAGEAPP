package com.ubo.tp.message.ihm.ListUserComponent;

import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.core.database.IDatabaseObserver;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;
import com.ubo.tp.message.ihm.MessageAppMainView;
import com.ubo.tp.message.ihm.messageComponent.*;
import com.ubo.tp.message.ihm.userComponent.UserModel;

public class ListUserController implements IDatabaseObserver {
    private UserModel userModel;
    private MainView mainView;
    private ListListener listListener;

    private ModelData modelData;

    private ListService service;

    private MessageAppMainView mMain;

    private User currentUser ;

    private IDatabase db ;

    public ListUserController(UserModel userModel , MainView mainView , ModelData modelData, ListService service , User currentUser , IDatabase db){

        this.userModel= userModel;
        this.mainView = mainView;
        this.modelData = modelData;
        this.service = service;
        this.currentUser = currentUser;
        this.db = db;
        this.db.addObserver(this);

    }


    @Override
    public void notifyMessageAdded(Message addedMessage) {

    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {

    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {

    }

    @Override
    public void notifyUserAdded(User addedUser) {

    }

    @Override
    public void notifyUserDeleted(User deletedUser) {

    }

    @Override
    public void notifyUserModified(User modifiedUser) {
        this.service.updateFollowLists(this.currentUser , this.modelData);
    }
}
