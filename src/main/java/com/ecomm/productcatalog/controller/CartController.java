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

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem existingItem = cartRepo.findByUserIdAndProductId(user.getId(), productId);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepo.save(existingItem);
        } else {
            CartItem newItem = new CartItem(user.getId(), product.getId(), 1);

            cartRepo.save(newItem);
        }
    }

    @PutMapping("/set-quantity/{productId}")
    public void updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable String productId,
                               @RequestParam int quantity) {
        if (quantity < 1) return;

        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        CartItem item = cartRepo.findByUserIdAndProductId(user.getId(), productId);
        if (item != null) {
            item.setQuantity(quantity);
            cartRepo.save(item);
        }
    }

    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        List<CartItem> items = cartRepo.findByUserId(user.getId());

        // Populate each CartItem with its corresponding Product
        for (CartItem item : items) {
            productRepo.findById(item.getProductId()).ifPresent(item::setProduct);
        }

        return items;
    }


    @DeleteMapping("/remove/{productId}")
    public void removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable String productId) {
        User user = userRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        cartRepo.deleteByUserIdAndProductId(user.getId(), productId);
    }
}
