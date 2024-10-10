package com.sharmachait.shoppingcart.service.cart;

import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity) throws ResourceNotFoundException;
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
}
