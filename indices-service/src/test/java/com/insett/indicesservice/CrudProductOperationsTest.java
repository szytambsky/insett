package com.insett.indicesservice;

import com.insett.indicesservice.domain.dao.ProductRepository;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CrudProductOperationsTest extends AbstractIndicesServiceTests {
    private static final Logger log = LoggerFactory.getLogger(CrudProductOperationsTest.class);

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeEach
    public void deleteAndRefreshEsDb() {
        repository.deleteAll();
        elasticsearchOperations.indexOps(Product.class).refresh();
    }

    @Test
    public void crudOperations() {
        var givenTitle = "Riftbound Origin Box";
        var givenDescription = "sam.cards@example.com";
        var givenCategory = "TCG - Trading Card Games";
        var givenBrand = "Riot";
        var givenPrice = 129;
        var givenStockQuantity = 10;
        Product product = createProduct(givenTitle, givenDescription, givenCategory, givenBrand, givenPrice, givenStockQuantity);

        repository.save(product);
        elasticsearchOperations.indexOps(Product.class).refresh();
        printAll();
        product = repository.findByTitleAndBrand(givenTitle, givenBrand).orElseThrow();
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

    @Test
    public void buildCrud() {
        List<Product> list = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> createProduct("product-" + i, "description-" + i,
                        "category-" + i, "brand-" + i, i * 10, i))
                .toList();
        repository.saveAll(list);
        elasticsearchOperations.indexOps(Product.class).refresh();
        printAll();
        Assertions.assertEquals(10, repository.count());

        List<Integer> findList = List.of(2, 4, 6);
        list = findList.stream()
                .flatMap(i ->
                        repository
                                .findAllByTitleAndCategory(
                                        "product-" + i,
                                        "category-" + i
                                )
                                .stream()
                )
                .toList();
        printAll();
        Assertions.assertEquals(3, list.size());

        List<String> ids = new ArrayList<>();
        Function<Integer, Integer> doublingThePrice = price -> {  return price * 2; };
        list.forEach(p -> p.setPrice(doublingThePrice.apply(p.getPrice())));
        this.repository.saveAll(list);
        elasticsearchOperations.indexOps(Product.class).refresh();
        printAll();
        findList.stream()
                .flatMap(i ->
                        repository.findAllByTitleAndCategory(
                                        "product-" + i,
                                        "category-" + i
                                )
                                .stream()
                ).forEach(p -> {
                    ids.add(p.getId());
                    Assertions.assertEquals(p.getPrice() / 20, p.getInStock());
                });
        printAll();

        repository.deleteAllById(ids);
        Assertions.assertEquals(7, repository.count());
        printAll();
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
