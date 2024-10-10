package com.sharmachait.shoppingcart.controller;

import com.sharmachait.shoppingcart.dtos.ApiResponse;
import com.sharmachait.shoppingcart.dtos.ProductDto;
import com.sharmachait.shoppingcart.dtos.add.AddProductDto;
import com.sharmachait.shoppingcart.dtos.update.UpdateProductDto;
import com.sharmachait.shoppingcart.model.Product;
import com.sharmachait.shoppingcart.service.product.IProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RestController
@RequestMapping("${api.prefix}/product")
@Slf4j
public class ProductController {
    @Autowired
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        try{
            List<Product> products = productService.getAllProducts();
            List<ProductDto> productDtos = productService.productsToDtos(products);
            return ResponseEntity.ok(new ApiResponse("Found " + products.size() + " products", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try{
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.productToDto(product);
            return ResponseEntity.ok(new ApiResponse("Found!!", productDto));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand){
        try{
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDto> productDtos = productService.productsToDtos(products);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products with this brand!", null));
            return ResponseEntity.ok(new ApiResponse("Found!!", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/brand/{brand}/name/{name}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(
            @PathVariable String brand, @PathVariable String name){
        try{
            List<Product> products = productService.getProductsByBrandAndName(brand,name);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products with this brand!", null));
            List<ProductDto> productDtos = productService.productsToDtos(products);
            return ResponseEntity.ok(new ApiResponse("Found!!", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/brand/{brand}/category/{category}")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(
            @PathVariable String brand, @PathVariable String category){
        try{
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products with this brand!", null));
            List<ProductDto> productDtos = productService.productsToDtos(products);
            return ResponseEntity.ok(new ApiResponse("Found!!", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try{
            List<Product> products = productService.getProductsByName(name);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products with this brand!", null));
            List<ProductDto> productDtos = productService.productsToDtos(products);
            return ResponseEntity.ok(new ApiResponse("Found!!", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category){
        try{
            List<Product> products = productService.getProductsByCategory(category);
            if(products.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No products with this brand!", null));
            List<ProductDto> productDtos = productService.productsToDtos(products);
            return ResponseEntity.ok(new ApiResponse("Found!!", productDtos));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductDto productDto){
        try{
            Product productAdded = productService.addProduct(productDto);
            ProductDto productDtoAdded = productService.productToDto(productAdded);
            return ResponseEntity.ok(new ApiResponse("Added product", productDtoAdded));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProductById(
            @PathVariable Long productId, @RequestBody UpdateProductDto productDto){
        try{
            Product product = productService.updateProduct(productDto,productId);
            ProductDto productDtoUpdated = productService.productToDto(product);
            return ResponseEntity.ok(new ApiResponse("Updated!!", productDtoUpdated));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No such product found!", null));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId){
        try{
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Deleted!!", null));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("No such product found!", null));
        }
    }

    @GetMapping("/count/brand/{brand}/name/{name}")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(
            @PathVariable String brand, @PathVariable String name){
        try{
            long count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Count", count));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}