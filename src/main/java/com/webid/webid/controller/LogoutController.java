package com.webid.webid.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

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
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear cookie 
        Cookie cookie = new Cookie("jwtToken", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Use this if you're on HTTPS
        cookie.setMaxAge(0); // Immediately expire the cookie
        response.addCookie(cookie);


        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            logoutService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }

}
