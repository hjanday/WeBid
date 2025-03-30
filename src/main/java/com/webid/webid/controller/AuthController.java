package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.dto.LoginUserDTO;
import com.webid.webid.dto.RegisterUserDTO;
import com.webid.webid.dto.UpdatePasswordRequestDTO;
import com.webid.webid.model.RoleEnum;
import com.webid.webid.model.User;
import com.webid.webid.responses.LoginResponse;
import com.webid.webid.service.AuthService;
import com.webid.webid.service.JwtService;
import jakarta.servlet.http.Cookie;


import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletResponse;

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
	public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO user, HttpServletResponse response) {
		User authenticatedUser = authService.signIn(user);

		Map<String, Object> claims = new HashMap<>();
    	claims.put("roles", authenticatedUser.getRoles()); 

		System.out.println("User roles before token generation: " + authenticatedUser.getRoles());

		String jwtToken = jwtService.generateToken(claims, authenticatedUser);

		  // Create a cookie with the token
		  Cookie cookie = new Cookie("jwtToken", jwtToken);
		  cookie.setPath("/"); // cookie is available to all paths in your domain
		  cookie.setHttpOnly(true); // prevents JavaScript access to the cookie
		  cookie.setSecure(true); // ensure this is true if you use HTTPS
		  cookie.setMaxAge(2700); // ttl in seconds //45 minutes
		  

		LoginResponse login_response = new LoginResponse(jwtToken, jwtService.getJwtTTL(), authenticatedUser.getRoles());
		// add cookie to servlet response
		response.addCookie(cookie);
		return ResponseEntity.ok(login_response);
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
