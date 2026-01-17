package com.ecommerce_backend.security;

import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;




@Service
public class CustomerUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {

  User user =    userRepository.findByEmail(email).
              orElseThrow(() -> new ResourceNotFoundException ("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // encoded password
                .roles(user.getRole().name()) // USER / ADMIN
                .build();
    }
}
