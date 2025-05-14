package com.ecomm.productcatalog.controller;

import com.ecomm.productcatalog.model.Product;
import com.ecomm.productcatalog.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size,
                                                     @RequestParam(required = false) String search) {
        System.out.println("GET /api/products?page=" + page + "&size=" + size + "&search=" + search);

        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Product> products;

            if (search != null && !search.isBlank()) {
                // Try exact (paginated) match first
                var pageResult = productRepo.findByProductNameContainingIgnoreCase(search, pageable);
                products = pageResult.getContent();

                if (products.isEmpty()) {
                    // Fallback to fuzzy across fields (unpaginated)
                    System.out.println("No direct matches, using fuzzy fallback search.");
                    products = productRepo.searchFuzzyAcrossFields(search);
                }
            } else {
                products = productRepo.findAll(pageable).getContent();
            }

            System.out.println("Returning " + products.size() + " products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }






    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Validate required fields based on your schema
        if (product.getId() == null ||
                product.getProductName() == null ||
                product.getPriceCurrent() == null ||
                product.getSku() == null) {
            return ResponseEntity.badRequest().build();
        }

        Product saved = productRepo.save(product);
        return ResponseEntity.ok(saved);
    }


}
