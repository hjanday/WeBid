package com.webid.webid.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webid.webid.service.LogoutService;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            logoutService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }

}
