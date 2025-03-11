package com.ubo.tp.message.ihm.ListUserComponent;

import com.ubo.tp.message.datamodel.User;

public interface ListListener {
    public void follow(User userToFollow);
    public void unFollow(User userToUnFollow);
    public void back();
}
