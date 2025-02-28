package com.webid.webid.service;
import com.webid.webid.model.User;
import com.webid.webid.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
public class AuthService {

private final UserRepository userRepository;

public AuthService(UserRepository userRepository) {
	this.userRepository = userRepository;
}

public Optional<User> signIn(String user, String pw){
	Optional<User> usr = userRepository.findByUsername(user);
	if(usr.isPresent() && usr.get().getPassword().equals(pw)) {
		return usr;
	}
	return Optional.empty();
}

public User signUp(User usr) {
	if (userRepository.findByUsername(usr.getUsername()).isPresent()) {
		throw new RuntimeException("Username already exists.");
	}
	if (userRepository.findByEmail(usr.getEmail()).isPresent()) {
		throw new RuntimeException("Email already exists.");
	}
	return userRepository.save(usr);
}

	
}
