package com.webid.notification_microservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserNotifs(@PathVariable Long userId) {
        List<String> notifications = notificationService.getUserNotifications(userId);

        if (notifications.isEmpty()) {
            return ResponseEntity.ok("No notifications.");
        }

        return ResponseEntity.ok(notifications);
    }

    @PostMapping("completed/{auctionID}")
    public ResponseEntity<Object> completedAuction(@PathVariable long auctionID) {
        try {
            notificationService.notifyAuctionEnded(auctionID);
            return ResponseEntity.ok("Notifications have been sent regarding the ending of the auction.");
        } catch (Error e) {
            return ResponseEntity.badRequest().body("An error occured: " + e.getMessage());
        }
    }

}
