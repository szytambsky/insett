package com.insett.indicesservice;

import com.insett.indicesservice.domain.dao.ProductRepository;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CrudProductOperationsTest extends AbstractIndicesServiceTests {
    private static final Logger log = LoggerFactory.getLogger(CrudProductOperationsTest.class);

    @Autowired
    private ProductRepository repository;

    @Test
    public void crudOperations() {
        var givenTitle = "Riftbound Origin Box";
        var givenDescription = "sam.cards@gmail.com";
        var givenCategory = "TCG - Trading Card Games";
        var givenBrand = "Riot";
        var givenPrice = 129;
        var givenStockQuantity = 10;
        Product product = createProduct(givenTitle, givenDescription, givenCategory, givenBrand, givenPrice, givenStockQuantity);

        repository.save(product);
        printAll();
        product = repository.findProductByTitleAndBrand(givenTitle, givenBrand).orElseThrow();
        Assertions.assertEquals(givenTitle, product.getTitle());
        Assertions.assertEquals(givenDescription, product.getDescription());
        Assertions.assertEquals(givenPrice, product.getPrice());

        product.setPrice(229);
        product.setInStock(3);
        product = repository.save(product);
        printAll();
        Assertions.assertEquals(229, product.getPrice());
        Assertions.assertEquals(3, product.getInStock());

        repository.deleteById(product.getId());
        Assertions.assertFalse(repository.existsById(product.getId()));
    }

    private Product createProduct(String title, String description,
                                  String category, String brand,
                                  Integer price, Integer stockQuantity) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setCategory(category);
        product.setBrand(brand);
        product.setPrice(price);
        product.setInStock(stockQuantity);
        return product;
    }

    private void printAll() {
        repository.findAll()
                .forEach(product -> log.info("product: {}", product));
    }
}
