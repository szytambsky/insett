package com.insett.warehouseservice.core.application.product.mapper;


import com.insett.warehouseservice.adapters.persistence.ProductDto;
import com.insett.warehouseservice.adapters.persistence.ProductRequest;
import com.insett.warehouseservice.core.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    @Mappings({
            @Mapping(source = "productName", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "price", target = "price"),
            @Mapping(source = "imageUrl", target = "imageUrl"),
    })
    ProductDto toDto(Product product);
}
