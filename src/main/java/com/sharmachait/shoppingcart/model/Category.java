package com.sharmachait.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {
  @Id

  private Long id;
  private String name;


//  @JsonManagedReference
  @JsonIgnore
  @OneToMany(mappedBy = "category")
  private List<Product> products;
}
