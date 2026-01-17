package com.ecommerce_backend.controller;


import com.ecommerce_backend.dto.*;


import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.repository.UserRepository;
import com.ecommerce_backend.service.UserService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {



    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public UserController(UserService userService , AuthenticationManager authenticationManager , UserRepository userRepository) {


        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<?>> register(
            @RequestBody UserRequestDTO dto) {

        UserResponseDTO response = userService.userRegisterUser(dto);

        APIResponse<UserResponseDTO> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("User registered successfully");
        apiResponse.setData(response);



        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponseDTO>> loginUser(@RequestBody LoginRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );


        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Build response
        LoginResponseDTO responseDto = new LoginResponseDTO();
        responseDto.setId(user.getId());
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole().name());
        responseDto.setMessage("Login successful");

        APIResponse<LoginResponseDTO> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Login successful");
        apiResponse.setData(responseDto);

        return ResponseEntity.ok(apiResponse);
    }




    @GetMapping("/{id}")
    public  ResponseEntity<APIResponse<UserResponseDTO>>getUserByid(@PathVariable Long id) {
        UserResponseDTO dto=userService.getUserById(id);
        APIResponse<UserResponseDTO> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("User found successfully");
        apiResponse.setData(dto);

        return ResponseEntity.ok(apiResponse);


    }
    @GetMapping("/list")
    public ResponseEntity<APIResponse<List<UserResponseDTO>>> getAllUsers() {

        List<UserResponseDTO> users = userService.getAllUsers();

        APIResponse<List<UserResponseDTO>> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Users fetched successfully");
        response.setData(users);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        APIResponse<Void> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("User deleted successfully");
        response.setData(null);

        return ResponseEntity.ok(response);
    }




}


