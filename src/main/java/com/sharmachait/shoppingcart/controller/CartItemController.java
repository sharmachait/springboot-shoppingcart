package com.sharmachait.shoppingcart.controller;

import com.sharmachait.shoppingcart.dtos.ApiResponse;
import com.sharmachait.shoppingcart.service.cart.ICartItemService;
import com.sharmachait.shoppingcart.service.cart.ICartService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("${api.prefix}/cartItem")
public class CartItemController {
    @Autowired
    private final ICartItemService cartItemService;
    @Autowired
    private final ICartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            if(cartId==null){
                cartId = cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to cart", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Item was not added to cart: "+e.getMessage(), null));
        }
    }

    @DeleteMapping("/removeFromCart")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId,
                                                     @RequestParam Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Item was not removed from cart: "+e.getMessage(), null));
        }
    }

    @PutMapping("/updateItemQuantity")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam Long cartId,
                                                          @RequestParam Long productId,
                                                          @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Item was not removed from cart: "+e.getMessage(), null));
        }
    }
}
