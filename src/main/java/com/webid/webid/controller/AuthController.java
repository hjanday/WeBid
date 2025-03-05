package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.dto.LoginUserDTO;
import com.webid.webid.dto.RegisterUserDTO;
import com.webid.webid.dto.UpdatePasswordRequestDTO;
import com.webid.webid.model.User;
import com.webid.webid.responses.LoginResponse;
import com.webid.webid.service.AuthService;
import com.webid.webid.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService auth, JwtService jwtService) {
		this.authService = auth;
		this.jwtService = jwtService;
	}

	// @PostMapping("/register")
	// public ResponseEntity<?> register(@RequestBody User usr) {
	// 	try {
	// 		User newUsr = authService.signUp(usr);
	// 		return ResponseEntity.ok(newUsr);
	// 	} catch (RuntimeException e) {
	// 		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	// 	}
	// }

	// @PostMapping("/login")
	// public ResponseEntity<?> login(@RequestBody User usr) {
	// 	Optional<User> loginUser = auth.signIn(usr.getUsername(), usr.getPassword());
	// 	if (loginUser.isPresent()) {
	// 		UserDetails userDetails = loginUser.get();
	// 		String jwtToken = jwtService.generateToken(userDetails);
	// 		return ResponseEntity.ok(jwtToken);
	// 	} else {
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

	// 	}
	// }

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO user) {
		User authenticatedUser = authService.signIn(user);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginResponse response = new LoginResponse(jwtToken, jwtService.getJwtTTL());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterUserDTO newUser) {
		User registeredUser = authService.signUp(newUser);
		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestDTO usrReq) {
		Optional<User> currentUser = authService.updatePassword(usrReq.getEmail(), usrReq.getNewPassword());
		if (currentUser.isPresent()) {
			return ResponseEntity.ok(currentUser.get());
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

		}
	}

}
