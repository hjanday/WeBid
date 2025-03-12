package com.webid.webid.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

@Service
public class NotificationService implements Observer {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void notify(User user, String message) {
        user.setNotif(message);
    }

    public String getNotification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getNotif();
    }
}
