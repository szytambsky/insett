package com.insett.warehouseservice.adapters.api;

import com.insett.warehouseservice.adapters.api.dto.ProductRequest;
import com.insett.warehouseservice.adapters.api.dto.ProductDto;
import com.insett.warehouseservice.core.application.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        log.info("Creating product {}", productRequest.productName());
        ProductDto newProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(newProduct);
    }
}
