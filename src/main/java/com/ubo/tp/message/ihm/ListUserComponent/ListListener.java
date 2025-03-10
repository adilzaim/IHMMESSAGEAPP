package main.java.com.ubo.tp.message.ihm.ListUserComponent;

import main.java.com.ubo.tp.message.datamodel.User;

public interface ListListener {
    public void follow(User userToFollow);
    public void unFollow(User userToUnFollow);
    public void back();
}
