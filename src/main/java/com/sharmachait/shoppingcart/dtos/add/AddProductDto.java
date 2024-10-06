package com.sharmachait.shoppingcart.dtos.add;

import com.sharmachait.shoppingcart.model.Category;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddProductDto {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;
}
