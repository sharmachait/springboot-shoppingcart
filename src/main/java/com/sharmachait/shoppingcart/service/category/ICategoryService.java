package com.sharmachait.shoppingcart.service.category;

import com.sharmachait.shoppingcart.dtos.add.AddCategoryDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateCategoryDto;
import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(AddCategoryDto category);
    Category updateCategory(UpdateCategoryDto category, Long id) throws ResourceNotFoundException;
    void deleteCategoryById(Long id) throws ResourceNotFoundException;
}
