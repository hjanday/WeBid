package com.webid.webid.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webid.webid.model.User;
import com.webid.webid.model.Notification;
import com.webid.webid.repository.UserRepository;
import com.webid.webid.repository.NotificationRepository;

@Service
public class NotificationService implements Observer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void notify(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);

        // Save notification in notification DB
        notificationRepository.save(notification);

        // set most recent notification to be the user's current notification
        user.setNotif(message);
        userRepository.save(user);
    }

    public String getNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getNotif();
    }

    // returns a list of user notifications
    public List<String> getUserNotifications(User user) {
        return notificationRepository.findByUserIdOrderByIdDesc(user.getId()).stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());
    }

}
