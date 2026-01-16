package com.ecommerce_backend.repository;

import com.ecommerce_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository   extends JpaRepository<Product, Long> {


    List<Product> findByCategoryId(Long category);


}
