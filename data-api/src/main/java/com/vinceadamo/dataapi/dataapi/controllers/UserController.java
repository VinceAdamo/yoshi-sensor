package com.vinceadamo.dataapi.dataapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.vinceadamo.dataapi.dataapi.entities.User;
import com.vinceadamo.dataapi.dataapi.repositories.UserRepository;
import com.vinceadamo.dataapi.dataapi.requests.UserRequest;
import com.vinceadamo.dataapi.dataapi.services.JwtService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest req)  {
        String email = req.getEmail();
        String password = req.getPassword();

        if (userRepository.findOneByEmail(email) != null) {
            return ResponseEntity.badRequest().body("Email Address already in use!");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (isUserValid(email, password)) {
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    private boolean isUserValid(String email, String password) {
        User user = userRepository.findOneByEmail(email);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
}