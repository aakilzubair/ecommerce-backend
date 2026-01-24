package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.*;
import com.ecommerce_backend.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<APIResponse<CartResponseDTO>> addToCart(
            @RequestBody CartRequestDTO cartRequestDTO) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        CartResponseDTO cartResponse =
                cartService.addToCart(email, cartRequestDTO);

        APIResponse<CartResponseDTO> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Product added to cart successfully");
        response.setData(cartResponse);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    @PostMapping("/view")
    public ResponseEntity<APIResponse<List<CartItemViewResponseDTO>>> viewCart(
            @RequestBody CartItemVIewRequestDTO requestDTO) {

        List<CartItemViewResponseDTO> cartItems =
                cartService.viewCart(requestDTO);

        APIResponse<List<CartItemViewResponseDTO>> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Cart items fetched successfully");
        response.setData(cartItems);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update-quantity")
    public ResponseEntity<APIResponse<Void>> updateQuantity(
            @RequestBody CartItemVIewRequestDTO requestDTO) {

        cartService.updateQuantity(requestDTO);

        APIResponse<Void> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Cart item quantity updated");
        response.setData(null);

        return ResponseEntity.ok(response);
    }


    // 2️⃣ REMOVE ITEM
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<APIResponse<Void>> removeItem(
            @PathVariable Long cartItemId) {

        cartService.removeItem(cartItemId);

        APIResponse<Void> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Cart item removed");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

}


