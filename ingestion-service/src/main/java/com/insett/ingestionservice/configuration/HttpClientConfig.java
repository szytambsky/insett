package com.insett.ingestionservice.configuration;

import com.insett.ingestionservice.services.CardIngestionClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration(proxyBeanMethods = true)
public class HttpClientConfig {

    @Bean
    public CardIngestionClient cardIngestionClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
                .requestFactory(clientHttpRequestFactory())
                .build();
        RestClientAdapter clientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(clientAdapter).build();
        return httpServiceProxyFactory.createClient(CardIngestionClient.class);
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.of(5, ChronoUnit.SECONDS));
        factory.setConnectTimeout(Duration.of(30, ChronoUnit.SECONDS));
        return factory;
    }
}
