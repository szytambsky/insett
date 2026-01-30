package com.insett.indicesservice;

import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    /**
     * Provide a Testcontainers Elasticsearch container preconfigured for tests.
     *
     * The container uses the Elasticsearch 9.0.2 Docker image and has security
     * and HTTP SSL disabled to simplify integration testing.
     *
     * @return an {@link ElasticsearchContainer} configured with the 9.0.2 image and with security and HTTP SSL disabled
     */
    @Bean
    @ServiceConnection
    ElasticsearchContainer elasticsearchContainer() {
        return new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:9.0.2"))
                .withEnv("xpack.security.enabled", "false")
                .withEnv("xpack.security.http.ssl.enabled", "false");
    }

    /**
     * Provide a Jackson ObjectMapper instance for use in tests.
     *
     * @return a new {@link com.fasterxml.jackson.databind.ObjectMapper} instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}