package com.webid.webid.service;
import com.webid.webid.dto.LoginUserDTO;
import com.webid.webid.dto.RegisterUserDTO;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;

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

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authManager = authManager;
	}

	public User signIn(LoginUserDTO input){
		authManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(),input.getPassword()));

		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

	public User signUp(RegisterUserDTO input){
		User newUser = new User();
		newUser.setFirstName(input.getFirstName());
		newUser.setLastName(input.getLastName());
		newUser.setEmail(input.getEmail());
		newUser.setUsername(input.getUsername());
		newUser.setPassword(passwordEncoder.encode(input.getPassword()));

		return userRepository.save(newUser);
	}

	public Optional<User> updatePassword(String userEmail, String newPw) {
		Optional<User> u = userRepository.findByEmail(userEmail);
		if(u.isPresent()) {
			userRepository.updatePasswordByEmail(userEmail, newPw);
			return u;
		}
		return Optional.empty();
	}

	
}
