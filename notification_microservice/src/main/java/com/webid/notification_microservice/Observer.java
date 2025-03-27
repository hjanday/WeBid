package com.webid.notification_microservice;

import com.webid.webid.model.User;

public interface Observer {
    void notify(User user,String message);
}
