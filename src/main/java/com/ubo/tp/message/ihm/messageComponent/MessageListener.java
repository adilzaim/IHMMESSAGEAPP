package main.java.com.ubo.tp.message.ihm.messageComponent;

import java.util.Date;

public interface MessageListener {
    void onMessageSend(String tag, String content );
}
