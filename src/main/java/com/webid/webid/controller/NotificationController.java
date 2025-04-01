package com.webid.webid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.User;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.NotificationService;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/notify")
    public void notify(@RequestBody Long id,@RequestBody String notif){
        notificationService.notify(id,notif);
    }
    @PostMapping("/completed/{auctionID}")
    public ResponseEntity<Object> completedAuction(@PathVariable long auctionID) {
        try {
            notificationService.notifyEnded(auctionID);
            return ResponseEntity.ok("Notifications have been sent regarding the ending of the auction.");
        } catch (Error e) {
            return ResponseEntity.badRequest().body("An error occured: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Object> deleteAuctions(@CurrentUser User currentUser) {
        try {
            notificationService.deleteNotifications(currentUser);
            return ResponseEntity.ok("Notifications successfully deleted");
        } catch (Error e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
