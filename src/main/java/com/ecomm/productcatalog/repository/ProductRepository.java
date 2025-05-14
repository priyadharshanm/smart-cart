package com.ecomm.productcatalog.repository;

import com.ecomm.productcatalog.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    @Query("{ $or: [ " +
            "{ 'productName': { $regex: ?0, $options: 'i' } }, " +
            "{ 'category': { $regex: ?0, $options: 'i' } }, " +
            "{ 'brand': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Product> searchFuzzyAcrossFields(String query);
}
