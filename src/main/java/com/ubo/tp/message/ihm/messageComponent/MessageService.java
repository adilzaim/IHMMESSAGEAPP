package com.ubo.tp.message.ihm.messageComponent;

import com.ubo.tp.message.core.EntityManager;
import com.ubo.tp.message.core.database.Database;
import com.ubo.tp.message.core.database.IDatabase;
import com.ubo.tp.message.datamodel.Message;
import com.ubo.tp.message.datamodel.User;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private IDatabase db;
    public MessageService(IDatabase db){
        this.db = db;
    }

    public void createMessage(String tag , String content , EntityManager entityManager){
        User user = null;
        for(User i : db.getUsers()){
            if(i.getUserTag().equals(tag)) user = i;
        }

        if(user != null){
            Message msg = new Message(user,content);
            entityManager.writeMessageFile(msg);
        }
    }

    public List<Message> getMessageForUser(User user) {
        List<Message> liste = new ArrayList<>();
        for(Message msg : this.db.getUserMessages(user)){
            liste.add(msg);

        }
        //toAddMessagesFollowing
        for(User u : this.db.getUsers()){
            if(user.isFollowing(u)) {
                for(Message msg : this.db.getUserMessages(u)){
                    liste.add(msg);

                }
            }
        }
        return liste;
    }
}
