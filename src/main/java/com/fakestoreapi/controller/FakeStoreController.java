package com.fakestoreapi.controller;

import com.fakestoreapi.model.CartValueResponse;
import com.fakestoreapi.model.NeighborsDistanceResponse;
import com.fakestoreapi.model.carts.Cart;
import com.fakestoreapi.model.products.Product;
import com.fakestoreapi.model.users.User;
import com.fakestoreapi.service.FakeStoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FakeStoreController {

    private final FakeStoreService fakeStoreService;

    public FakeStoreController(FakeStoreService fakeStoreService) {
        this.fakeStoreService = fakeStoreService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@Value("${fakestore.api.users}") String usersUrl) {
        return fakeStoreService.getData(usersUrl, User[].class);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<Cart>> getCarts(@Value("${fakestore.api.carts}") String cartsUrl) {
        return fakeStoreService.getData(cartsUrl, Cart[].class);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@Value("${fakestore.api.products}") String productsUrl) {
        return fakeStoreService.getData(productsUrl, Product[].class);
    }

    @GetMapping("/categories-and-values")
    public Map<String, BigDecimal> getCategoryValues() {
        return fakeStoreService.getCategoryValues();
    }

    @GetMapping("/highest-value-cart")
    public CartValueResponse findCartWithHighestValue() {
        return fakeStoreService.findCartWithHighestTotalValue();
    }

    @GetMapping("/furthest-neighbors")
    public NeighborsDistanceResponse findFurthestNeighbors() {
        return fakeStoreService.findFurthestNeighbors();
    }

}
