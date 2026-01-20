package com.ecommerce_backend.controller;


import com.ecommerce_backend.dto.APIResponse;
import com.ecommerce_backend.dto.ProductRequestDTO;
import com.ecommerce_backend.dto.ProductResponseDTO;

import com.ecommerce_backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse<ProductResponseDTO>> addProduct(
            @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO product =
                productService.addProduct(productRequestDTO);

        APIResponse<ProductResponseDTO> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Product added successfully");
        response.setData(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }




    @GetMapping("/category/{id}")
    public ResponseEntity<APIResponse<List<ProductResponseDTO>>> getProductsByCategory(
            @PathVariable Long id) {

        List<ProductResponseDTO> products =
                productService.getProductsByCategory(id);

        APIResponse<List<ProductResponseDTO>> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Products fetched by category");
        response.setData(products);


        return ResponseEntity.ok(response);
    }


}
