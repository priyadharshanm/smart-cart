package com.ecomm.productcatalog.service;
import com.ecomm.productcatalog.model.Product;
import org.springframework.stereotype.Service;
import com.ecomm.productcatalog.repository.ProductRepository;

import java.util.List;

@Service
public class ProductSearchService {

    private final ProductRepository productRepo;

    public ProductSearchService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> searchProducts(String input) {
        return productRepo.searchFuzzyAcrossFields(input);
    }
}

