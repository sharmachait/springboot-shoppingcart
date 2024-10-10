package com.sharmachait.shoppingcart.service.cart;

import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Cart;
import com.sharmachait.shoppingcart.repository.CartItemRepository;
import com.sharmachait.shoppingcart.repository.CartRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Data
@Service
@Slf4j
public class CartService implements ICartService {
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartItemRepository cartItemRepository;
    @Override
    public Cart getCart(Long id) throws ResourceNotFoundException {
        try{
            Cart cart = cartRepository.findById(id).get();
            BigDecimal total = cart.getTotalAmount();
            cart.updateTotalAmount();
            return cartRepository.save(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public void clearCart(Long id) throws ResourceNotFoundException {
        try {
            Cart cart = getCart(id);
            cartItemRepository.deleteAllByCartId(id);
            cart.getCartItems().clear();
            cartRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getTotalPrice(Long id) throws ResourceNotFoundException {
        try {
            Cart cart = getCart(id);
//            cart.updateTotalAmount();
            return cart.getTotalAmount();
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
