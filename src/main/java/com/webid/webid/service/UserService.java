package com.webid.webid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Get auction by ID
    public Optional<User> getUserbyEmail(String email) {
        String cleanEmail = email.replaceAll("^\"|\"$", "").trim();
        return userRepository.findByEmail(cleanEmail);
    }

    public Optional<User> getUserbyUsername(String un) {
        String cleanUN = un.replaceAll("^\"|\"$", "").trim();
        return userRepository.findByUsername(cleanUN);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
