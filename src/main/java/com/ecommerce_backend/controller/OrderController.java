package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.APIResponse;
import com.ecommerce_backend.dto.OrderResponseDTO;
import com.ecommerce_backend.dto.PlaceOrderRequestDTO;
import com.ecommerce_backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place")
    public ResponseEntity<APIResponse<OrderResponseDTO>> placeOrder(
            @RequestBody(required = false) PlaceOrderRequestDTO requestDTO) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName(); // ✅ JWT subject

        OrderResponseDTO order =
                orderService.placeOrder(email, requestDTO);

        APIResponse<OrderResponseDTO> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Order placed successfully");
        response.setData(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
