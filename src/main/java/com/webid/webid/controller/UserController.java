package com.webid.webid.controller;

import org.springframework.web.bind.annotation.*;

import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

}
