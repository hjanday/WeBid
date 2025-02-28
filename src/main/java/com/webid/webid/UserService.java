package com.webid.webid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(String username, String password, String firstName, String lastName,
            String address, String city, String postalCode, String country, String email) {

        // You can add validation logic here if necessary (e.g., check if the username
        // is unique, etc.)

        // Create a new User object with the provided fields
        User user = new User(username, password, firstName, lastName, address, city, postalCode, country, email);

        // Save the user to the database and return the saved user
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
