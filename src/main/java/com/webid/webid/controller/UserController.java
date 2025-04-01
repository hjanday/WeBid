package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.JwtService;
import com.webid.webid.service.NotificationService;
import com.webid.webid.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final NotificationService notificationService;
    private final UserService userService;

    public UserController(
            NotificationService notificationService,
            JwtService jwtService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> findAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{userID}")
    public Optional<User> getUserInfo(@PathVariable Long userID) {
        return this.userService.getUserById(userID);
    }

    @GetMapping("/notify")
    public String getNotif() {
        return notificationService.getNotification();
    }
    
    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable long userID) {
        userService.deleteUser(userID);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return this.userService.saveUser(user);
    }

    @GetMapping("/currentuser")
    public ResponseEntity<User> getCurrentUser(@CurrentUser User currentUser) {
        System.out.println(currentUser);
        if (currentUser == null) {
            System.out.println("User is null! Authentication failed.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/findusername")
    ResponseEntity<User> queryUsername(@RequestBody String username) {
        return this.userService.getUserbyUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/findemail")
    ResponseEntity<User> queryEmail(@RequestBody String email) {
        return this.userService.getUserbyEmail(email).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
