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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository,
            NotificationService notificationService,
            JwtService jwtService, UserService userService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping
    public Iterable<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/{userID}")
    public Optional<User> getUserInfo(@PathVariable Long userID) {
        return this.userRepository.findById(userID);
    }
    

    @GetMapping("/notify")
    public String getNotif() {
        return notificationService.getNotification();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }
    @GetMapping("/currentuser")
    public User getCurrentUser(@CurrentUser User currentUser){
        return currentUser;
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

}
