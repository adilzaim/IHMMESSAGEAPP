package com.ubo.tp.message.ihm.listener;

public interface LoginListener {
    void loginVerify(String username , String tag);
    void createUser(String name, String tag , String avatarPath, String password);
}
