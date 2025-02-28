package com.webid.webid.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.webid.webid.model.User;

import com.webid.webid.service.AuthService;


import org.springframework.http.ResponseEntity;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService auth;
	public AuthController(AuthService auth) {
		this.auth = auth;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User usr){
		try {
			User newUsr = auth.signUp(usr);
			return ResponseEntity.ok(newUsr);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User usr){
		Optional<User> loginUser = auth.signIn(usr.getUsername(), usr.getPassword());
		if (loginUser.isPresent()) {
			return ResponseEntity.ok(loginUser.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
			
		}
		
	}

}
