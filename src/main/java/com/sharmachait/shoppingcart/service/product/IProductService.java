package com.sharmachait.shoppingcart.service.product;

import com.sharmachait.shoppingcart.dtos.add.AddProductDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateProductDto;
import com.sharmachait.shoppingcart.exceptions.ProductNotFoundException;
import com.sharmachait.shoppingcart.model.Product;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDto addProductDto);
    Product getProductById(Long id) throws ProductNotFoundException;
    void deleteProductById(Long id) throws ProductNotFoundException;
    Product updateProduct(UpdateProductDto updateProductDto, Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand,String name);

}
