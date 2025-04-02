package com.webid.webid.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webid.webid.dto.LoginUserDTO;
import com.webid.webid.dto.RegisterUserDTO;
import com.webid.webid.dto.UpdatePasswordRequestDTO;
import com.webid.webid.model.RoleEnum;
import com.webid.webid.model.User;
import com.webid.webid.responses.LoginResponse;
import com.webid.webid.security.CurrentUser;
import com.webid.webid.service.AuthService;
import com.webid.webid.service.JwtService;
import jakarta.servlet.http.Cookie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserDetailsService userDetailsService;
	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService auth, JwtService jwtService, UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
		this.authService = auth;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDTO user, HttpServletResponse response) {
		try {
			// Sign in user
			User authenticatedUser = authService.signIn(user);

			// Create a roles claim that will be stored in the JWT token
			Map<String, Object> claims = new HashMap<>();
			claims.put("roles", authenticatedUser.getRoles()); 

			// Generate JWT token
			String jwtToken = jwtService.generateToken(claims, authenticatedUser);

			// Create a cookie with the token
			Cookie cookie = new Cookie("jwtToken", jwtToken);
			cookie.setPath("/"); // cookie is available to all paths in your domain
			cookie.setHttpOnly(true); // prevents JavaScript access to the cookie
			cookie.setSecure(true); // ensure this is true if you use HTTPS
			cookie.setMaxAge(3600); // ttl in seconds

			// add cookie to servlet response
			response.addCookie(cookie);

			LoginResponse login_response = new LoginResponse(jwtToken, jwtService.getJwtTTL(), authenticatedUser.getRoles(), "Login successful!");
			
			return ResponseEntity.ok(login_response);
		}
		catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, 0, null, "Login failed! Invalid email or password."));
		}
		
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterUserDTO newUser) {
		// Register user
		User registeredUser = authService.signUp(newUser);
		
		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestDTO usrReq) {
		// Update password
		authService.updatePassword(usrReq.getEmail(), usrReq.getNewPassword());
		
		// Create response
		Map<String, String> response = new HashMap<>();
		response.put("message", "Password was updated successfully!");
		
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh-cookies")
	public ResponseEntity<?> refreshAllCookies(@CookieValue(name = "jwtToken", required = false) String authToken, HttpServletResponse response, @CurrentUser User currentUser) {
		
		Map<String, String> res = new HashMap<>();
	
		// Validate the current auth token
		if (authToken == null || authToken.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
		}
		
		try {
			// Extract username from the token
			String username = jwtService.extractUsername(authToken);
			User user = (User) this.userDetailsService.loadUserByUsername(username);
			
			// Get user details
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
			}
			
			// Validate the token
			if (!jwtService.isTokenValid(authToken, user)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
			}
			
			// Generate a new token with a fresh expiration time
			String newToken = jwtService.generateToken(user);
			
			// Set the new token as a cookie
			Cookie cookie = new Cookie("jwtToken", newToken);
			cookie.setMaxAge(3600); // 1 hour
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setSecure(true); // For HTTPS
			
			response.addCookie(cookie);
			res.put("jwtToken", newToken);
			res.put("message", "Token refreshed successfully!");
			
			return ResponseEntity.ok(res);
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error refreshing token: " + e.getMessage());
		}
	}		

}
