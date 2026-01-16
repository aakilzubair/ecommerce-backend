package com.ecommerce_backend.service;


import com.ecommerce_backend.dto.ProductRequestDTO;
import com.ecommerce_backend.dto.ProductResponseDTO;
import com.ecommerce_backend.entity.Category;
import com.ecommerce_backend.entity.Product;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.CategoryRepository;
import com.ecommerce_backend.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public   ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
         this.productRepository = productRepository;
         this.categoryRepository = categoryRepository;
     }




     public ProductResponseDTO addProduct(ProductRequestDTO dto) {

         Category category = categoryRepository.findById(dto.getCategoryId())
                 .orElseThrow(() ->
                         new ResourceNotFoundException("Category not found")
                 );
         Product product = new Product();
         BeanUtils.copyProperties(dto, product);
         Product saved = productRepository.save(product);
         ProductResponseDTO productResponseDTO = new ProductResponseDTO();
         BeanUtils.copyProperties(saved, productResponseDTO);
         return productResponseDTO;


     }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found");
        }

        List<Product> products =
                productRepository.findByCategoryId(categoryId);

        List<ProductResponseDTO> response = new ArrayList<>();

        for (Product product : products) {
            ProductResponseDTO dto = new ProductResponseDTO();
            BeanUtils.copyProperties(product, dto);
            // optional
            // dto.setCategoryName(product.getCategory().getName());
            response.add(dto);
        }

        return response; // can be empty
    }



}
