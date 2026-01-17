package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.LoginRequestDTO;
import com.ecommerce_backend.dto.LoginResponseDTO;
import com.ecommerce_backend.dto.UserRequestDTO;
import com.ecommerce_backend.dto.UserResponseDTO;
import com.ecommerce_backend.entity.Role;
import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.exception.DuplicateResourceException;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;


    }
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;




    public UserResponseDTO userRegisterUser(UserRequestDTO dto) {

        if(userRepository.existsByEmail(dto.getEmail())) {
           throw new DuplicateResourceException("email already exists");
        }

        User user= new User();

        BeanUtils.copyProperties(dto, user);

        user.setRole(Role.USER);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        User savedUser=  userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();

       BeanUtils.copyProperties(savedUser,response);
       return response;
    }



   public LoginResponseDTO loginUser(LoginRequestDTO dto) {

       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       dto.getEmail(),
                       dto.getPassword()
               )
       );


       User user = userRepository.findByEmail(dto.getEmail())
               .orElseThrow(() -> new RuntimeException("User not found"));


       LoginResponseDTO response = new LoginResponseDTO();
       response.setId(user.getId());
       response.setUsername(user.getUsername());
       response.setEmail(user.getEmail());
       response.setRole(user.getRole().name());
       response.setMessage("Login successful");

       return response;
   }

   public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(user,response);

        return response;
   }




    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> response = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO responseDTO = new UserResponseDTO();
            BeanUtils.copyProperties(user, responseDTO);
            response.add(responseDTO);
        }
        return response;
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("USER NOT FOUND")
                );

        userRepository.delete(user);
    }



}


