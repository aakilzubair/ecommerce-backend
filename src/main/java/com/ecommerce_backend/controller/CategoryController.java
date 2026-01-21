package com.ecommerce_backend.controller;

import com.ecommerce_backend.dto.APIResponse;
import com.ecommerce_backend.dto.CategoryRequestDto;
import com.ecommerce_backend.dto.CategoryResponseDto;

import com.ecommerce_backend.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;
     public CategoryController(CategoryService categoryService) {
         this.categoryService = categoryService;

     }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<APIResponse<CategoryResponseDto>> addCategory(
            @RequestBody CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto saved =
                categoryService.addCategory(categoryRequestDto);

        APIResponse<CategoryResponseDto> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Category created successfully");
        response.setData(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDto categoryRequestDto) {

        CategoryResponseDto updated =
                categoryService.updateCategory(id, categoryRequestDto);

        APIResponse<CategoryResponseDto> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Category updated successfully");
        response.setData(updated);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

        List<CategoryResponseDto> response = categoryService.getAllCategories();

        return ResponseEntity.ok(response); // 200 OK
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDto>> getCategoryById(
            @PathVariable Long id) {

        CategoryResponseDto category =
                categoryService.getCategoryById(id);

        APIResponse<CategoryResponseDto> response = new APIResponse<>();
        response.setSuccess(true);
        response.setMessage("Category fetched successfully");
        response.setData(category);

        return ResponseEntity.ok(response); // 200 OK
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }


}







