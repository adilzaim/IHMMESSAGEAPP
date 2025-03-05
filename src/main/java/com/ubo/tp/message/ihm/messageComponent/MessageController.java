package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;
import main.java.com.ubo.tp.message.ihm.userComponent.UserModel;

import java.util.List;

public class MessageController  {
    private UserModel userModel;
    private MessagePanel messageView;
    private MessageAnnouncementView messageAnnouncementView;
    private MessageService messageService;
    private MessageListener listener;
    private FilterListener filterListener;
    public MessageController(UserModel user , MessagePanel messageView , IDatabase db, MessageAppMainView mMainView , MessageAnnouncementView messageAnnouncementView){
        this.messageService = new MessageService(db);
        this.messageView = messageView;
        this.userModel = user;
        this.initializeListener();
        this.messageView.setMessageListener(this.listener);
        mMainView.setMessageView(this.messageView);
        this.messageAnnouncementView = messageAnnouncementView;
        this.initializeFilterListener();
        mMainView.setRightBottomComponent(this.messageAnnouncementView);
    }

    public void initializeListener(){
        this.listener = new MessageListener() {
            @Override
            public void onMessageSend(String tag, String content) {
                messageService.createMessage(tag,content);
            }
        };
    }

    public void initializeFilterListener() {
        this.filterListener = new FilterListener() {
            @Override
            public void onFilter(String query) {
                // Récupérer tous les messages depuis le service
                List<Message> allMessages = messageService.getMessageForUser(userModel.getCurrentUser());

                // Appliquer le filtrage en fonction de la requête
                List<Message> filteredMessages;
                if (query == null || query.trim().isEmpty()) {
                    filteredMessages = allMessages;
                } else {
                    String lowerQuery = query.toLowerCase().trim();
                    filteredMessages = allMessages.stream()
                            .filter(msg -> matchesQuery(msg, lowerQuery))
                            .toList();
                }

                // Mettre à jour l'affichage des messages filtrés
                messageAnnouncementView.unsetMessageList(filteredMessages);
            }

            // Fonction utilitaire pour matcher un message avec la requête
            private boolean matchesQuery(Message msg, String query) {
                if (query.startsWith("@")) {
                    String userTag = query.substring(1);
                    return msg.getSender().getUserTag().toLowerCase().contains(userTag) ||
                            msg.getText().toLowerCase().contains("@" + userTag);
                } else if (query.startsWith("#")) {
                    String tag = query.substring(1);
                    return msg.getText().toLowerCase().contains("#" + tag);
                }
                return msg.getSender().getUserTag().toLowerCase().contains(query) ||
                        msg.getText().toLowerCase().contains(query);
            }

            @Override
            public void onFilterRemove(){
                messageAnnouncementView.unsetMessageList(messageService.getMessageForUser(userModel.getCurrentUser()));
            }
        };

        // Associer le filterListener à la vue
        messageAnnouncementView.setFilterListener(this.filterListener);
    }



}
