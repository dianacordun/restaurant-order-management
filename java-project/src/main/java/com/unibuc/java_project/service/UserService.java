package com.unibuc.java_project.service;

import com.unibuc.java_project.model.Authority;
import com.unibuc.java_project.model.User;
import com.unibuc.java_project.repository.AuthorityRepository;
import com.unibuc.java_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public List<String> registerUser(User user) {
        List<String> errors = new ArrayList<>();

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.add("Passwords do not match.");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            errors.add("Username already exists.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);

        Authority authority = new Authority();
        authority.setUser(user);
        authority.setAuthority("ROLE_USER");
        authorityRepository.save(authority);

        return errors; // empty list = success
    }
}

