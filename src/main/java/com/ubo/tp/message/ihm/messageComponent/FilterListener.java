package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.datamodel.User;

public interface FilterListener {
    void onFilter(String query);
    void onFilterRemove();
}
