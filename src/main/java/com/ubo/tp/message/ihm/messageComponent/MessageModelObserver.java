package main.java.com.ubo.tp.message.ihm.messageComponent;

import java.util.List;

public interface MessageModelObserver {
    /**
     * Méthode appelée lorsqu'il y a une mise à jour de la liste des messages.
     *
     * @param updatedList La nouvelle liste des messages.
     */
    void onMessageListUpdated(List<Object> updatedList);
}

