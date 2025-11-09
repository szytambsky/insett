package com.insett.warehouseservice.adapters.persistence;

import com.insett.warehouseservice.core.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product save(Product product);
}
