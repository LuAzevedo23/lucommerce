package com.luazevedo.lucommerce.repositories;

import com.luazevedo.lucommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
