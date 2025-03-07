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
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserbyUsername(String un) {
        return userRepository.findByUsername(un);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
