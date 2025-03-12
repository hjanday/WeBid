package com.webid.webid.service;

import com.webid.webid.dto.LoginUserDTO;
import com.webid.webid.dto.RegisterUserDTO;
import com.webid.webid.exceptions.ResourceAlreadyExistsException;
import com.webid.webid.exceptions.ResourceNotFoundException;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authManager;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authManager = authManager;
	}

	public User signIn(LoginUserDTO input) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

	public User signUp(RegisterUserDTO input) {

		// Check for input validation prior to setting data
		if (userRepository.findByEmail(input.getEmail()).isPresent()) {
			throw new ResourceAlreadyExistsException("Email already registered!");
		}
		if (userRepository.findByUsername(input.getUsername()).isPresent()) {
			throw new ResourceAlreadyExistsException("Username already exists!");
		}

		try {
			User newUser = new User();
			newUser.setFirstName(input.getFirstName());
			newUser.setLastName(input.getLastName());
			newUser.setEmail(input.getEmail());
			newUser.setUsername(input.getUsername());
			newUser.setPassword(passwordEncoder.encode(input.getPassword()));
			newUser.setAddress(input.getAddress());
			newUser.setPostalCode(input.getPostalCode());
			newUser.setCountry(input.getCountry());
			newUser.setCity(input.getCity());

			return userRepository.save(newUser);
			// If anything else errors - catch it
		} catch (DataIntegrityViolationException ex) {
			throw new ResourceAlreadyExistsException("Data Integrity Error: " + ex.getMessage());
		}
	}

	public void updatePassword(String userEmail, String newPw) {
		Optional<User> u = userRepository.findByEmail(userEmail);
		// Check if record does not exist and throw exception
		if (u.equals(Optional.empty())) {
			throw new ResourceNotFoundException("Record with email: " + userEmail + " not found.");
		}
		try {
			String encodedPassword = passwordEncoder.encode(newPw);
			userRepository.updatePasswordByEmail(userEmail, encodedPassword);
		} catch (DataIntegrityViolationException ex) {
			throw new ResourceAlreadyExistsException("Data Integrity Error: " + ex.getMessage());
		}
	}

}
