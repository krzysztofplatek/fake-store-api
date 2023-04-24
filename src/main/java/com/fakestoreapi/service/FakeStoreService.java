package com.fakestoreapi.service;

import com.fakestoreapi.model.CartValueResponse;
import com.fakestoreapi.model.NeighborsDistanceResponse;
import com.fakestoreapi.model.carts.Cart;
import com.fakestoreapi.model.products.Product;
import com.fakestoreapi.model.users.User;
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

import java.math.BigDecimal;
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

    public Map<String, BigDecimal> getCategoryValues() {
        Map<String, BigDecimal> categoryMap;

        try {
            ResponseEntity<List<Product>> response = getData(productsUrl, Product[].class);
            Optional<List<Product>> optionalProducts = Optional.ofNullable(response.getBody());
            List<Product> products = optionalProducts.orElse(Collections.emptyList());

            categoryMap = new HashMap<>(products.stream()
                    .collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(p -> BigDecimal.valueOf(p.getPrice()), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))));
        } catch (Exception e) {
            throw new RuntimeException("Error while proccesing data: " + e.getMessage());
        }

        return categoryMap;
    }

    public CartValueResponse findCartWithHighestTotalValue() {

        ResponseEntity<List<User>> usersResponse = getData(usersUrl, User[].class);
        Optional<List<User>> optionalUsers = Optional.ofNullable(usersResponse.getBody());
        List<User> users = optionalUsers.orElse(Collections.emptyList());

        ResponseEntity<List<Cart>> cartsResponse = getData(cartsUrl, Cart[].class);
        Optional<List<Cart>> optionalCarts = Optional.ofNullable(cartsResponse.getBody());
        List<Cart> carts = optionalCarts.orElse(Collections.emptyList());

        ResponseEntity<List<Product>> productsResponse = getData(productsUrl, Product[].class);
        Optional<List<Product>> optionalProducts = Optional.ofNullable(productsResponse.getBody());
        Map<Integer, Product> productsMap = optionalProducts.orElse(Collections.emptyList()).stream().collect(Collectors.toMap(Product::getId, product -> product));

        double highestTotal = 0;
        User highestTotalOwner = new User();
        int cartId = 0;

        for (Cart cart : carts) {
            double total = Arrays.stream(cart.getProducts())
                    .mapToDouble(product -> {
                        Product productDetails = productsMap.get(product.getProductId());
                        double price = productDetails.getPrice();
                        return price * product.getQuantity();
                    })
                    .sum();

            if (total > highestTotal) {
                highestTotal = total;
                cartId = cart.getId();
                highestTotalOwner = users.get(cart.getUserId() - 1);
            }
        }

        return new CartValueResponse(cartId, highestTotalOwner.getName().getFirstname() + " " + highestTotalOwner.getName().getLastname(), highestTotal);
    }

    public NeighborsDistanceResponse findFurthestNeighbors() {

        ResponseEntity<List<User>> usersResponse = getData(usersUrl, User[].class);
        Optional<List<User>> optionalUsers = Optional.ofNullable(usersResponse.getBody());
        List<User> users = optionalUsers.orElse(Collections.emptyList());

        double maxDistance = 0;
        User userA = new User();
        User userB = new User();

        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                User user1 = users.get(i);
                User user2 = users.get(j);

                double distance = calculateDistance(user1.getAddress().getGeolocation().getLat(),
                        user1.getAddress().getGeolocation().getLng(),
                        user2.getAddress().getGeolocation().getLat(),
                        user2.getAddress().getGeolocation().getLng());

                if (distance > maxDistance) {
                    maxDistance = distance;
                    userA = user1;
                    userB = user2;
                }
            }
        }

        String userAName = userA.getName().getFirstname() + " " + userA.getName().getLastname();
        String userBName = userB.getName().getFirstname() + " " + userB.getName().getLastname();

        return new NeighborsDistanceResponse(userAName, userBName, maxDistance);
    }


    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}