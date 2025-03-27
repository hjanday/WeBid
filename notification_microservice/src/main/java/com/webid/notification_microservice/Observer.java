package com.webid.notification_microservice;

public interface Observer {
    void notify(Long userId, String message);
}
