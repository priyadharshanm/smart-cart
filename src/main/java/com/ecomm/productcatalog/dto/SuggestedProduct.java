package com.ecomm.productcatalog.dto;

import com.ecomm.productcatalog.model.Product;

public class SuggestedProduct {
    private Product product;
    private int quantity;

    public SuggestedProduct() {}

    public SuggestedProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
