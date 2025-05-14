package com.ecomm.productcatalog.controller;

import com.ecomm.productcatalog.repository.UserRepository;
import com.ecomm.productcatalog.security.JwtUtil;
import com.ecomm.productcatalog.model.User;

import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ecomm.productcatalog.controller.RegisterRequest;
import com.ecomm.productcatalog.controller.LoginRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already taken";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        System.out.println("Received login request:");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password: " + request.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                request.getUsername(),
                request.getPassword(),
                java.util.Collections.emptyList()
        );

        return jwtUtil.generateToken(userDetails.getUsername());
    }
}
