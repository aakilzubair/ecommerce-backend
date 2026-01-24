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
import com.ecommerce_backend.security.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager , JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;


    }





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
       String token = jwtService.generateToken(
               user.getEmail(),
               user.getRole().name()
       );
       LoginResponseDTO response = new LoginResponseDTO();
       response.setId(user.getId());
       response.setUsername(user.getUsername());
       response.setEmail(user.getEmail());
       response.setRole(user.getRole().name());
       response.setMessage("Login successful");
       response.setToken(token);

       return response;
   }

   public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(user,response);

        return response;
   }




    public List<UserResponseDTO> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort =
                direction.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDTO> response = new ArrayList<>();

        for (User user : userPage.getContent()) {
            UserResponseDTO dto = new UserResponseDTO();
            BeanUtils.copyProperties(user, dto);
            response.add(dto);
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

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        User savedUser = userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());


        return response;
    }




}


