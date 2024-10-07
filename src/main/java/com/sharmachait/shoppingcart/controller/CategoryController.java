package com.sharmachait.shoppingcart.controller;

import com.sharmachait.shoppingcart.dtos.ApiResponse;
import com.sharmachait.shoppingcart.dtos.add.AddCategoryDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateCategoryDto;
import com.sharmachait.shoppingcart.model.Category;
import com.sharmachait.shoppingcart.service.category.ICategoryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("${api.prefix}/category")
@Slf4j
public class CategoryController {
    @Autowired
    private final ICategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found!",categories));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No categories found", null));
        }
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable Long categoryId){
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found!",category));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No category found", null));
        }
    }
    @GetMapping("/{Name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found!",category));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No category found", null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody AddCategoryDto category){
        try{
            Category addedCategory = categoryService.addCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Category added", addedCategory));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("Can not create, category with this name already exists",null));
        }
    }
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody UpdateCategoryDto categoryDto){
        try {
            categoryService.updateCategory(categoryDto, categoryId);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully!",null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No category found", null));
        }
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId){
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.status(HttpStatus.GONE).body(new ApiResponse("Deleted Successfully!",null));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No category found", null));
        }
    }
}
