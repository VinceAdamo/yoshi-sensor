package com.vinceadamo.dataapi.dataapi.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vinceadamo.dataapi.dataapi.entities.User;
import com.vinceadamo.dataapi.dataapi.entities.UserInfoDetails;
import com.vinceadamo.dataapi.dataapi.repositories.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email not found" + email);
        }
        return new UserInfoDetails(user);
    }
}