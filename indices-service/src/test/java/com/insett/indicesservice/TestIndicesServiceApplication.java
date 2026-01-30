package com.insett.indicesservice;

import org.springframework.boot.SpringApplication;

public class TestIndicesServiceApplication {

    /**
     * Bootstraps the Indices service test application with Testcontainers configuration.
     *
     * Delegates startup to {@code IndicesServiceApplication#main}, applies {@code TestcontainersConfiguration},
     * and runs the application with the provided command-line arguments.
     *
     * @param args command-line arguments forwarded to the application
     */
    public static void main(String[] args) {
        SpringApplication.from(IndicesServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}