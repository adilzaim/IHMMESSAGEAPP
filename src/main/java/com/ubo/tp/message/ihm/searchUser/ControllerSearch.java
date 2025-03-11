package com.ubo.tp.message.ihm.searchUser;

import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.ArrayList;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;
import main.java.com.ubo.tp.message.ihm.userComponent.UserMapView;
public class ControllerSearch {

    private IDatabase db ;
    private SearchUserModel model;
    private SearchUserView view;
    private MessageAppMainView mainView;
    private UserMapView userMapView;
    public ControllerSearch(IDatabase db, SearchUserModel model, SearchUserView view, MessageAppMainView mMainView, UserMapView userMapView){

        this.db = db;
        this.model = model;
        this.view = view;
        this.mainView = mMainView;
        this.userMapView = userMapView;
        this.initialiseListener();

    }

    public void initialiseListener() {
        SearchListener searchListener = new SearchListener() {
            @Override
            public void onSearch(String searchTerm) {
                // Traitement à exécuter lors d'une recherche
                ArrayList<User> list = new ArrayList<>();
                for (User user : db.getUsers()){
                    if(user.getUserTag().contains(searchTerm)){
                        list.add(user);
                    }
                }

                model.setUsers(list);
            }

            @Override
            public void reset() {
                ArrayList<User> list = new ArrayList<>();
                for (User user : db.getUsers()){

                        list.add(user);

                }

                model.setUsers(list);
            }

            @Override
            public void backTo() {
                mainView.setUserMapView(userMapView);
            }


        };

        // Associer ce listener à la vue
        this.view.setSearchListener(searchListener);
    }
}
