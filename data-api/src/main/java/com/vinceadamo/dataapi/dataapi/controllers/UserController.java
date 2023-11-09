package com.vinceadamo.dataapi.dataapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.vinceadamo.dataapi.dataapi.entities.User;
import com.vinceadamo.dataapi.dataapi.repositories.UserRepository;
import com.vinceadamo.dataapi.dataapi.requests.UserRequest;
import com.vinceadamo.dataapi.dataapi.responses.NoBodyResponse;
import com.vinceadamo.dataapi.dataapi.responses.UserLoginResponse;
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
    public ResponseEntity<?> register(@RequestBody UserRequest req)  {
        String email = req.getEmail();
        String password = req.getPassword();

        if (userRepository.findOneByEmail(email) != null) {
            return ResponseEntity.badRequest().body(
                new NoBodyResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Email Address already in use!"
                )
            );
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);

        return ResponseEntity.ok(
            new NoBodyResponse(
                HttpStatus.OK.value(),
                "User registered successfully"
            )
        );
    }


    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findOneByEmail(email);

        if (isUserValid(user, password)) {
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(
                new UserLoginResponse(
                    email,
                    token
                )
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new NoBodyResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid credentials"
            )
        );
    }

    private boolean isUserValid(User user, String password) {
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }
}