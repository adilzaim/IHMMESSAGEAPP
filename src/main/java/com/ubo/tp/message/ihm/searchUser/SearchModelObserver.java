package com.ubo.tp.message.ihm.searchUser;
import main.java.com.ubo.tp.message.datamodel.User;

import java.util.List;

public interface SearchModelObserver {
    void onSearchResultsUpdated(List<User> results);
}
