package com.ecommerce_backend.controller;


import com.ecommerce_backend.dto.*;


import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.repository.UserRepository;
import com.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<APIResponse<UserResponseDTO>> register(@Valid
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
    public ResponseEntity<APIResponse<LoginResponseDTO>> loginUser( @Valid @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO responseDto = userService.loginUser(dto);

        APIResponse<LoginResponseDTO> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Login successful");
        apiResponse.setData(responseDto);

        return ResponseEntity.ok(apiResponse);
    }



    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public  ResponseEntity<APIResponse<UserResponseDTO>>getUserByid(@PathVariable Long id) {
        UserResponseDTO dto=userService.getUserById(id);
        APIResponse<UserResponseDTO> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("User found successfully");
        apiResponse.setData(dto);

        return ResponseEntity.ok(apiResponse);


    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<APIResponse<List<UserResponseDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        List<UserResponseDTO> users =
                userService.getAllUsers(page, size, sortBy, direction);

        APIResponse<List<UserResponseDTO>> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Users fetched successfully");
        response.setData(users);

        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        APIResponse<Void> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("User deleted successfully");
        response.setData(null);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
           @Valid @RequestBody UserRequestDTO dto
    ) {
        UserResponseDTO updatedUser = userService.updateUser(id, dto);

        APIResponse<UserResponseDTO> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("User updated successfully");
        response.setData(updatedUser);

        return ResponseEntity.ok(response);
    }





}


