package com.sharmachait.shoppingcart.controller;

import com.sharmachait.shoppingcart.dtos.ApiResponse;
import com.sharmachait.shoppingcart.model.Cart;
import com.sharmachait.shoppingcart.service.cart.ICartService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/cart")
@Data
public class CartController {
    @Autowired
    private final ICartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try{
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Found!", cart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try{
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Cleared!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/total/{cartId}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            Cart cart = cartService.getCart(cartId);
            cart.updateTotalAmount();
            return ResponseEntity.ok(new ApiResponse("Found!", cart.getTotalAmount()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
