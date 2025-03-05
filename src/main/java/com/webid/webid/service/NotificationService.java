package com.webid.webid.service;

import java.util.ArrayList;

import org.jvnet.hk2.annotations.Service;

import com.webid.webid.model.User;
@Service
public class NotificationService implements Observer {
    @Override
    public void notify(User user, String message) {
        ArrayList<String> temp = user.getNotif();
        temp.add(message);
        user.setNotif(temp);
    }
}
