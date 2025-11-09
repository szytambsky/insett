package com.insett.warehouseservice.core.services;

import com.insett.warehouseservice.adapters.persistence.ProductDto;
import com.insett.warehouseservice.adapters.persistence.ProductRepository;
import com.insett.warehouseservice.adapters.persistence.ProductRequest;
import com.insett.warehouseservice.core.domain.model.Product;
import com.insett.warehouseservice.core.services.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductManager implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
}
