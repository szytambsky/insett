package com.insett.indicesservice;

import org.springframework.boot.SpringApplication;

public class TestIndicesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(IndicesServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
