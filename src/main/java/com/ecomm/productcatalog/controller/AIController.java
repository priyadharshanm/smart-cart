package com.ecomm.productcatalog.controller;

import com.ecomm.productcatalog.model.Product;
import com.ecomm.productcatalog.repository.ProductRepository;
import com.ecomm.productcatalog.service.AIService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;
    private final ProductRepository productRepo;

    public AIController(AIService aiService, ProductRepository productRepo) {
        this.aiService = aiService;
        this.productRepo = productRepo;
    }

    @PostMapping("/cart-suggestions")
    public List<Product> suggestCart(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        List<String> keywords = aiService.extractKeywords(prompt);

        Set<Product> results = new LinkedHashSet<>();
        for (String keyword : keywords) {
            List<Product> matches = productRepo.searchFuzzyAcrossFields(keyword);

            if (!matches.isEmpty()) {
                // Score each match
                Product best = matches.stream()
                        .max(Comparator.comparingInt(p -> scoreMatch(p, keyword)))
                        .orElse(matches.get(0));

                results.add(best);
            }
        }


        return new ArrayList<>(results);
    }

    private int scoreMatch(Product product, String keyword) {
        keyword = keyword.toLowerCase();

        int score = 0;
        if (product.getProductName() != null && product.getProductName().toLowerCase().contains(keyword)) score += 10;
        if (product.getCategory() != null && product.getCategory().toLowerCase().contains(keyword)) score += 6;
        if (product.getBrand() != null && product.getBrand().toLowerCase().contains(keyword)) score += 4;
        if (product.getDepartment() != null && product.getDepartment().toLowerCase().contains(keyword)) score += 2;

        return score;
    }


}
