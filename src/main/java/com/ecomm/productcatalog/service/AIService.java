package com.ecomm.productcatalog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    public List<String> extractKeywords(String prompt) {
        // Simulated for now — you'd call GPT here
        if (prompt.toLowerCase().contains("breakfast")) {
            return List.of("eggs", "milk", "bread");
        }

        // TODO: Replace with GPT/OpenAI API call
        return List.of(prompt); // naive fallback
    }
}
