package com.sharmachait.shoppingcart.service.cart;

import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id) throws ResourceNotFoundException;
    void clearCart(Long id) throws ResourceNotFoundException;
    BigDecimal getTotalPrice(Long id) throws ResourceNotFoundException;
}
