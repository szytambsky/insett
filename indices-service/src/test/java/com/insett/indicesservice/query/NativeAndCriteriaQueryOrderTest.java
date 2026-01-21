package com.insett.indicesservice.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.insett.indicesservice.AbstractIndicesServiceTests;
import com.insett.indicesservice.domain.dao.OrderRepository;
import com.insett.indicesservice.entity.Order;
import com.insett.indicesservice.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;

public class NativeAndCriteriaQueryOrderTest extends AbstractIndicesServiceTests {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ElasticsearchOperations operations;

    @BeforeAll
    public void dataSetup() {
        dataRemnantsAndRefresh();
        List<Order> orders
                = super.readResource("data/orders.json", new TypeReference<List<Order>>() {});
        repository.saveAll(orders);
        Assertions.assertEquals(10, repository.count());
    }

    public void dataRemnantsAndRefresh() {
        repository.deleteAll();
        operations.indexOps(Order.class).refresh();
    }
}
