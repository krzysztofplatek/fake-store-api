package com.fakestoreapi.service;

import com.fakestoreapi.model.carts.Cart;
import com.fakestoreapi.model.products.Product;
import com.fakestoreapi.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FakeStoreService {

    private final RestTemplate restTemplate;

    @Autowired
    private FakeStoreService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String USERS_API = "https://fakestoreapi.com/users";
    private static final String CARTS_API = "https://fakestoreapi.com/carts";
    private static final String PRODUCTS_API = "https://fakestoreapi.com/products";

    public List<User> getUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(USERS_API, User[].class);

        User[] users = response.getBody();

        if (users == null) return Collections.emptyList();

        return Arrays.asList(users);

    }

    public List<Cart> getCarts() {
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(CARTS_API, Cart[].class);

        Cart[] carts = response.getBody();

        if (carts == null) return Collections.emptyList();

        return Arrays.asList(carts);
    }

    public List<Product> getProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity(PRODUCTS_API, Product[].class);

        Product[] products = response.getBody();

        if (products == null) return Collections.emptyList();

        return Arrays.asList(products);
    }

}
