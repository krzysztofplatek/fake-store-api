package com.fakestoreapi.controller;

import com.fakestoreapi.model.carts.Cart;
import com.fakestoreapi.model.products.Product;
import com.fakestoreapi.model.users.User;
import com.fakestoreapi.service.FakeStoreService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class FakeStoreController {

    private final FakeStoreService fakeStoreService;

    public FakeStoreController(FakeStoreService fakeStoreService) {
        this.fakeStoreService = fakeStoreService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return fakeStoreService.getUsers();
    }

    @GetMapping("/carts")
    public List<Cart> getCarts() {
        return fakeStoreService.getCarts();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return fakeStoreService.getProducts();
    }

}
