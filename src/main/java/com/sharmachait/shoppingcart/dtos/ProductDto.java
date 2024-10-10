package com.sharmachait.shoppingcart.dtos;

import com.sharmachait.shoppingcart.model.Category;
import com.sharmachait.shoppingcart.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;
    private List<ImageDto> images;
}
