package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;
import com.webid.webid.service.JwtService;
import com.webid.webid.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository,
            NotificationService notificationService,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/notify")
    public String getNotif() {
        return notificationService.getNotification();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    @PostMapping("/findusername")
    ResponseEntity<User> queryUsername(@RequestBody String username) {
        return this.userRepository.findByUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/findemail")
    ResponseEntity<User> queryEmail(@RequestBody String email) {
        return this.userRepository.findByEmail(email).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/getdetails")
    public ResponseEntity<?> getTokenClaims(@RequestBody String token) {
        try {
            token = token.replaceAll("^\"|\"$", "");
            String username = jwtService.extractUsername(token);
            Optional<User> foundUser = userRepository.findByUsername(username);
            Long id = foundUser.get().getId();
            return ResponseEntity.ok(id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + ex.getMessage());
        }
    }

}
