package com.sharmachait.shoppingcart.repository;

import com.sharmachait.shoppingcart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
