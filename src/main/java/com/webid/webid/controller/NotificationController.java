package com.webid.webid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.User;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Object> getUserNotifs(@CurrentUser User currentUser) {
        List<String> notifications = notificationService.getUserNotifications(currentUser);

        if (notifications.isEmpty()) {
            return ResponseEntity.ok("No notifications.");
        }

        return ResponseEntity.ok(notifications);
    }
}
