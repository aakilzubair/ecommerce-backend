package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.CategoryRequestDto;
import com.ecommerce_backend.dto.CategoryResponseDto;
import com.ecommerce_backend.entity.Category;
import com.ecommerce_backend.exception.DuplicateResourceException;

import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {

        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new DuplicateResourceException( "CATEGORY ALREADY EXIST" );

        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryRequestDto, category);

        Category savedCategory = categoryRepository.save(category);

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        BeanUtils.copyProperties(savedCategory, categoryResponseDto);

        return categoryResponseDto;
    }


    public CategoryResponseDto updateCategory(
            Long id,
            CategoryRequestDto categoryRequestDto) {


        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CATEGORY NOT FOUND")
                );


        if (!existingCategory.getName().equals(categoryRequestDto.getName())
                && categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new DuplicateResourceException(
                    "Category with name already exists"
            );
        }


        existingCategory.setName(categoryRequestDto.getName());



        Category updatedCategory = categoryRepository.save(existingCategory);


        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        BeanUtils.copyProperties(updatedCategory, categoryResponseDto);

        return categoryResponseDto;
    }





    public List<CategoryResponseDto> getAllCategories() {

        List<Category> categories= categoryRepository.findAll();

        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
            BeanUtils.copyProperties(category, categoryResponseDto);
            categoryResponseDtos.add(categoryResponseDto);
        }
        return categoryResponseDtos;

    }

    public CategoryResponseDto getCategoryById(Long id) {

        Category category =   categoryRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("CATEGORY NOT FOUND"));

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        BeanUtils.copyProperties(category, categoryResponseDto);
        return categoryResponseDto;
    }

    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CATEGORY NOT FOUND")
                );

        categoryRepository.delete(category);
    }



}
