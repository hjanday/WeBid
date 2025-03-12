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

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService auth, JwtService jwtService) {
		this.authService = auth;
		this.jwtService = jwtService;
	}

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
		authService.updatePassword(usrReq.getEmail(), usrReq.getNewPassword());
		Map<String, String> response = new HashMap<>();
		response.put("message", "Password was updated successfully!");
		return ResponseEntity.ok(response);
	}

}
