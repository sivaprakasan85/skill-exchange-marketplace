package com.siva.skillexchange.service;

import com.siva.skillexchange.entity.User;
import com.siva.skillexchange.repository.UserRepository;
import com.siva.skillexchange.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.siva.skillexchange.repository.ListingRepository;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ListingRepository listingRepository;

    public User registerUser(User user) {

        // TC03: required field validation
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // TC02: duplicate email check
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash the password before saving — never store plain text
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // New users are active by default (already set in entity, but explicit here for clarity)
        user.setActive(true);

        // TC01: all good, save the user
        return userRepository.save(user);
    }

    public String login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Don't allow a soft-deleted (deactivated) user to log in
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getId());
    }

    public void deleteUser(Integer userId) {

        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Soft delete: mark inactive instead of removing the row.
        // Their listings/requests/reviews stay untouched — no FK errors, history preserved.
        user.setActive(false);
        userRepository.save(user);
    }
}