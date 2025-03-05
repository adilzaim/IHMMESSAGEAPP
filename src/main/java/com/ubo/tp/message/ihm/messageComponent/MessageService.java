package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.core.database.Database;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<Message> getMessageForUser(User user) {
        List<Message> liste = new ArrayList<>();
        for(Message msg : this.db.getUserMessages(user)){
            liste.add(msg);
        }
        //toAddMessagesFollowing
        return liste;
    }
}
