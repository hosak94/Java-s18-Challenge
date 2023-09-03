package com.workintech.jpa.onetomany.controller;

import com.workintech.jpa.onetomany.entity.Book;
import com.workintech.jpa.onetomany.entity.Category;
import com.workintech.jpa.onetomany.mapping.BookResponse;
import com.workintech.jpa.onetomany.mapping.CategoryResponse;
import com.workintech.jpa.onetomany.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            categoryResponses.add(new CategoryResponse(category.getId(),
                    category.getName()));
        }

        return categoryResponses;
    }

    @GetMapping("/{id}")
    public CategoryResponse find(@PathVariable int id) {
        Category category = categoryService.findById(id);
        if(category != null) {
            return new CategoryResponse(category.getId(), category.getName());
        }
        return null;

    }
    @PostMapping("/")
    public Category save(@RequestBody Category category) {
        return categoryService.save(category);
    }
    @PutMapping("/{id}")
    public CategoryResponse update(@RequestBody Category category, @PathVariable int id) {
        Category foundCategory = categoryService.findById(id);
        if(foundCategory != null){
            category.setId(id);
            categoryService.save(category);
            return new CategoryResponse(category.getId(), category.getName());
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public CategoryResponse delete(@PathVariable int id) {
        Category foundCategory = categoryService.findById(id);
        if(foundCategory != null) {
            categoryService.delete(foundCategory);
            return new CategoryResponse(foundCategory.getId(), foundCategory.getName());
        }
        return null;
    }

}
