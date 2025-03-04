package com.webid.webid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.webid.webid.model.Auction;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

public class UserService implements Observer{
    @Autowired
    private UserRepository userRepository;

 

    // Get auction by ID
    public Optional<User> getUserbyEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> getUserbyUsername(String un) {
        return userRepository.findByUsername(un);
    }



    @Override
    public void notify(User user, String message) {
        ArrayList<String> temp = user.getNotif();
        temp.add(message);
        user.setNotif(temp);
    }

}
