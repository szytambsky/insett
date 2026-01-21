package com.insett.indicesservice.domain.dao;

import com.insett.indicesservice.entity.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ElasticsearchRepository<Order, String> {
}
