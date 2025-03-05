package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModel;

public class MessageController {
    private UserModel userModel;
    private MessagePanel messageView;

    private MessageService messageService;
    private MessageListener listener;
    public MessageController(UserModel user , MessagePanel messageView , IDatabase db, MessageAppMainView mMainView){
        this.messageService = new MessageService(db);
        this.messageView = messageView;
        this.userModel = user;
        this.initializeListener();
        this.messageView.setMessageListener(this.listener);
        mMainView.setMessageView(this.messageView);

    }

    public void initializeListener(){
        this.listener = new MessageListener() {
            @Override
            public void onMessageSend(String tag, String content) {
                messageService.createMessage(tag,content);
            }
        };
    }
}
