package com.insett.warehouseservice.core.application.product;

import com.insett.warehouseservice.adapters.api.dto.ProductDto;
import com.insett.warehouseservice.adapters.api.dto.ProductRequest;


public interface ProductService {

    ProductDto createProduct(ProductRequest productRequest);
}
