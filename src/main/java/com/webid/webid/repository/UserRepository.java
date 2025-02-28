package com.webid.webid.repository;

import com.webid.webid.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByEmail(String email);
    // add find by username as well
    Optional<User> findByUsername(String username);
} 
