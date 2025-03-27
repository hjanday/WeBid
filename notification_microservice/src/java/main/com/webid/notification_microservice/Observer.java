package com.webid.webid.service;

import com.webid.webid.model.User;

public interface Observer {
    void notify(User user,String message);
}
