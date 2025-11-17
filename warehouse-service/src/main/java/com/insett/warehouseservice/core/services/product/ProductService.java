package com.insett.warehouseservice.core.services.product;

import com.insett.warehouseservice.adapters.persistence.ProductDto;
import com.insett.warehouseservice.adapters.persistence.ProductRequest;


public interface ProductService {

    ProductDto createProduct(ProductRequest productRequest);
}
