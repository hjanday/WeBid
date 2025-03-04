package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    @PostMapping("/findusername")
    ResponseEntity<User> queryUsername(@RequestBody String username) {
        String cleanUN = username.replaceAll("^\"|\"$", "").trim();
        return this.userRepository.findByUsername(cleanUN).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/findemail")
    ResponseEntity<User> queryEmail(@RequestBody String email) {
        String cleanEmail = email.replaceAll("^\"|\"$", "").trim();
        return this.userRepository.findByEmail(cleanEmail).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
