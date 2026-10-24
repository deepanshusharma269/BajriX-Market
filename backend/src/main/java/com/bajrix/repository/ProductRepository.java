package com.bajrix.repository;

import com.bajrix.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data will generate findByNameContainingIgnoreCase for search
}
