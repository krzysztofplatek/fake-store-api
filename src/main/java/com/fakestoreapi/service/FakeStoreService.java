package com.fakestoreapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FakeStoreService {

    private final RestTemplate restTemplate;
    private final String usersUrl;
    private final String cartsUrl;
    private final String productsUrl;

    private static final Logger logger = LoggerFactory.getLogger(FakeStoreService.class);

    public FakeStoreService(RestTemplate restTemplate,
                            @Value("${fakestore.api.users}") String usersUrl,
                            @Value("${fakestore.api.carts}") String cartsUrl,
                            @Value("${fakestore.api.products}") String productsUrl) {
        this.restTemplate = restTemplate;
        this.usersUrl = usersUrl;
        this.cartsUrl = cartsUrl;
        this.productsUrl = productsUrl;
    }

    public <T> ResponseEntity<List<T>> getData(String url, Class<T[]> responseType) {
        try {
            ResponseEntity<T[]> response = restTemplate.getForEntity(url, responseType);
            HttpStatusCode status = response.getStatusCode();

            Optional<T[]> responseBody = Optional.ofNullable(response.getBody());

            if (status == HttpStatus.OK && responseBody.isPresent()) {
                List<T> data = Arrays.stream(responseBody.get()).collect(Collectors.toList());
                return ResponseEntity.ok(data);
            } else if (status == HttpStatus.NO_CONTENT) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Error while fetching data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
