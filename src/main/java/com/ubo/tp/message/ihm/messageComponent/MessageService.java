package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.core.database.Database;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

public class MessageService {

    private IDatabase db;
    public MessageService(IDatabase db){
        this.db = db;
    }

    public void createMessage(String tag , String content ){
        User user = null;
        for(User i : db.getUsers()){
            if(i.getUserTag().equals(tag)) user = i;
        }

        if(user != null){
            Message msg = new Message(user,content);
            db.addMessage(msg);
        }
    }
}
