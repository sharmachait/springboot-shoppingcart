package com.sharmachait.shoppingcart.service.cart;

import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Cart;
import com.sharmachait.shoppingcart.model.CartItem;
import com.sharmachait.shoppingcart.model.Product;
import com.sharmachait.shoppingcart.repository.CartItemRepository;
import com.sharmachait.shoppingcart.repository.CartRepository;
import com.sharmachait.shoppingcart.service.product.IProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Data
public class CartItemService implements ICartItemService {

    @Autowired
    private final CartItemRepository cartItemRepository;
    @Autowired
    private final IProductService productService;
    @Autowired
    private final ICartService cartService;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) throws ResourceNotFoundException {
        Cart cart;
        Product product;
        try{
            cart = cartService.getCart(cartId);
            product = productService.getProductById(productId);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        CartItem cartItem;
        try {
            cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().equals(product)).findFirst().get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            cartItem = new CartItem();
        }
        if (cartItem.getId()==null){
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setCart(cart);
        }else{
            cartItem.setQuantity(quantity + cartItem.getQuantity());
        }

        try{
            cartItem.setTotalPrice();
            cart.addItem(cartItem);
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        try {
            CartItem cartItem = getCartItemFromCart(cartId, productId);
            if (cartItem == null) {
                throw new ResourceNotFoundException("CartItem not found");
            }
            Cart cart = cartService.getCart(cartId);
            cart.removeItem(cartItem);
            cartRepository.save(cart);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        try{
            Cart cart = cartService.getCart(cartId);
            CartItem cartItem = getCartItemFromCart(cartId, productId);
            if (cartItem == null) {
                throw new ResourceNotFoundException("CartItem not found");
            }
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice();
            cart.updateTotalAmount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CartItem getCartItemFromCart(Long cartId, Long productId){
        try{
            Cart cart = cartService.getCart(cartId);
            CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst().get();
            return cartItem;
        } catch (Exception e) {
            return null;
        }
    }
}
