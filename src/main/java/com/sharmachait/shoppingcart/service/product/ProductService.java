package com.sharmachait.shoppingcart.service.product;

import com.sharmachait.shoppingcart.dtos.ImageDto;
import com.sharmachait.shoppingcart.dtos.ProductDto;
import com.sharmachait.shoppingcart.dtos.add.AddProductDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateProductDto;
import com.sharmachait.shoppingcart.exceptions.ProductNotFoundException;
import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Category;
import com.sharmachait.shoppingcart.model.Image;
import com.sharmachait.shoppingcart.model.Product;
import com.sharmachait.shoppingcart.repository.CategoryRepository;
import com.sharmachait.shoppingcart.repository.ProductRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Data
public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Override
    public ProductDto productToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = product.getImages();
        List<ImageDto> imageDtos = images.stream()
                .map(image->modelMapper.map(image, ImageDto.class))
                .collect(Collectors.toList());
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public List<ProductDto> productsToDtos(List<Product> products) {
        return products.stream().map(product->productToDto(product)).collect(Collectors.toList());
    }

    @Override
    public Product addProduct(AddProductDto addProductDto) {

//        Category category = categoryRepository.findByName(productDto.getCategoryName())
//                .orElseGet(() -> {
//                    Category newCategory = new Category();
//                    newCategory.setName(productDto.getCategoryName());
//                    return categoryRepository.save(newCategory);
//                });
//        return createProduct(productDto, category);
        Category category = categoryRepository.findByName(addProductDto.getCategory().getName());
        category = getOrInsertCategory(category,addProductDto.getCategory().getName());
        Product product = createProduct(addProductDto, category);
        product = productRepository.save(product);
        return product;
    }
    private Category getOrInsertCategory(Category category, String categoryName){
        if(category == null) {
            category = new Category();
            category.setName(categoryName);
            category = categoryRepository.save(category);
        }
        return category;
    }
    private Product createProduct(AddProductDto addProductDto, Category category) {
        Product product = new Product();
        product.setName(addProductDto.getName());
        product.setBrand(addProductDto.getBrand());
        product.setPrice(addProductDto.getPrice());
        product.setInventory(addProductDto.getInventory());
        product.setDescription(addProductDto.getDescription());
        product.setCategory(category);
        product.setImages(new ArrayList<>());
        return product;
    }
    @Override
    public Product getProductById(Long id) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) throws ResourceNotFoundException {
//        productRepository.findById(id).ifPresentOrElse(
//                product -> productRepository.delete(product)
//                , () -> new ProductNotFoundException("Product not found")
//        );
        try{
            Product product = productRepository.findById(id).get();
            productRepository.delete(product);
        }catch(NoSuchElementException e){
            throw new ResourceNotFoundException("Product not found");
        }
    }

    @Override
    public Product updateProduct(UpdateProductDto updateProductDto, Long id) throws NoSuchElementException {
        Product existingProduct = productRepository.findById(id).get();
        if(existingProduct == null){
            existingProduct = new Product();
        }
        existingProduct = updateExistingProduct(existingProduct,updateProductDto);
        existingProduct = productRepository.save(existingProduct);
        return existingProduct;
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductDto updateProductDto) {
        existingProduct.setName(updateProductDto.getName());
        existingProduct.setBrand(updateProductDto.getBrand());
        existingProduct.setPrice(updateProductDto.getPrice());
        existingProduct.setInventory(updateProductDto.getInventory());
        existingProduct.setDescription(updateProductDto.getDescription());
        Category category = categoryRepository.findByName(updateProductDto.getCategory().getName());
        category = getOrInsertCategory(category,updateProductDto.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand( brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
