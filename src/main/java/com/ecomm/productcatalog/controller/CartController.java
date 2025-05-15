package com.ecomm.productcatalog.controller;

import com.ecomm.productcatalog.model.CartItem;
import com.ecomm.productcatalog.model.Product;
import com.ecomm.productcatalog.model.User;
import com.ecomm.productcatalog.repository.CartItemRepository;
import com.ecomm.productcatalog.repository.ProductRepository;
import com.ecomm.productcatalog.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public CartController(CartItemRepository cartRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/add/{productId}")
    public void addToCart(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable String productId) {
        System.out.println("POST /api/cart/add/" + productId);

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem existingItem = cartRepo.findByUserIdAndProductId(user.getId(), productId);

        if (existingItem != null) {
            System.out.println("Item already in cart. Incrementing quantity.");
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepo.save(existingItem);
        } else {
            System.out.println("Adding new item to cart.");
            CartItem newItem = new CartItem(user.getId(), product.getId(), 1);
            cartRepo.save(newItem);
        }
    }

    @PutMapping("/set-quantity/{productId}")
    public void updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable String productId,
                               @RequestParam int quantity) {
        System.out.println("PUT /api/cart/set-quantity/" + productId + "?quantity=" + quantity);

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        CartItem item = cartRepo.findByUserIdAndProductId(user.getId(), productId);

        if (quantity < 1) {
            if (item != null) {
                System.out.println("Quantity is 0. Removing item from cart.");
                cartRepo.deleteByUserIdAndProductId(user.getId(), productId);
            } else {
                System.out.println("Quantity is 0 but item not in cart. Nothing to delete.");
            }
        } else {
            if (item != null) {
                System.out.println("Item exists. Updating quantity to: " + quantity);
                item.setQuantity(quantity);
                cartRepo.save(item);
            } else {
                System.out.println("Item not in cart. Creating new item with quantity: " + quantity);
                CartItem newItem = new CartItem(user.getId(), productId, quantity);
                cartRepo.save(newItem);
            }
        }
    }


    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("GET /api/cart");

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        List<CartItem> items = cartRepo.findByUserId(user.getId());

        for (CartItem item : items) {
            productRepo.findById(item.getProductId()).ifPresent(item::setProduct);
        }

        System.out.println("Returning " + items.size() + " cart items.");
        return items;
    }

    @DeleteMapping("/remove/{productId}")
    public void removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable String productId) {
        System.out.println("DELETE /api/cart/remove/" + productId);

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        cartRepo.deleteByUserIdAndProductId(user.getId(), productId);

        System.out.println("Item removed from cart.");
    }
}
