package com.sharmachait.shoppingcart.service.category;

import com.sharmachait.shoppingcart.dtos.add.AddCategoryDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateCategoryDto;
import com.sharmachait.shoppingcart.exceptions.AlreadyExistsException;
import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Category;
import com.sharmachait.shoppingcart.repository.CategoryRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Data
@Slf4j
public class CategoryService implements ICategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        try{
            return categoryRepository.findById(id).get();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        try{
            return categoryRepository.findByName(name);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(AddCategoryDto category) throws AlreadyExistsException {
        Category existing = categoryRepository.findByName(category.getName());
        if(existing != null) {
            throw new AlreadyExistsException("Can not create, category with this name already exists");
        }
        else {
            Category newCategory = new Category();
            newCategory.setName(category.getName());
            newCategory = categoryRepository.save(newCategory);
            return newCategory;
        }
    }

    @Override
    public Category updateCategory(UpdateCategoryDto updateCategoryDto, Long id) throws ResourceNotFoundException {
        Category existingCategory = categoryRepository.findById(id).get();
        if(existingCategory == null){
            throw new ResourceNotFoundException("Category not found");
        }
        existingCategory = updateExistingCategory(existingCategory,updateCategoryDto);
        categoryRepository.save(existingCategory);
        return existingCategory;
    }
    private Category updateExistingCategory(Category existingCategory, UpdateCategoryDto updateCategoryDto) {
        existingCategory.setName(updateCategoryDto.getName());
        return existingCategory;
    }
    @Override
    public void deleteCategoryById(Long id) throws ResourceNotFoundException {
        try{
            Category category = categoryRepository.findById(id).get();
            categoryRepository.delete(category);
        }catch(NoSuchElementException e){
            throw new ResourceNotFoundException("Product not found");
        }
    }
}
