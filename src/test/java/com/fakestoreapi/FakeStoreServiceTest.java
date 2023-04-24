package com.fakestoreapi;

import java.math.BigDecimal;
import java.util.*;

import com.fakestoreapi.model.products.Product;
import com.fakestoreapi.model.products.Rating;
import com.fakestoreapi.model.users.User;
import com.fakestoreapi.service.FakeStoreService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FakeStoreServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FakeStoreService fakeStoreService;

    @Value("${fakestore.api.users}")
    private String usersUrl;

    @Value("${fakestore.api.carts}")
    private String cartsUrl;

    @Value("${fakestore.api.products}")
    private String productsUrl;


    @Before
    public void setUp() {
        fakeStoreService = new FakeStoreService(restTemplate, usersUrl, cartsUrl, productsUrl);
    }

    @Test
    public void testGetDataSuccess() {
        // Given
        User[] users = {new User(), new User()};
        ResponseEntity<User[]> response = ResponseEntity.ok(users);
        when(restTemplate.getForEntity(usersUrl, User[].class)).thenReturn(response);

        // When
        ResponseEntity<List<User>> result = fakeStoreService.getData(usersUrl, User[].class);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    public void testGetDataNoContent() {
        // Given
        ResponseEntity<User[]> response = ResponseEntity.noContent().build();
        when(restTemplate.getForEntity(usersUrl, User[].class)).thenReturn(response);

        // When
        ResponseEntity<List<User>> result = fakeStoreService.getData(usersUrl, User[].class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void testGetDataInternalServerError() {
        // Given
        when(restTemplate.getForEntity(usersUrl, User[].class))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        ResponseEntity<List<User>> result = fakeStoreService.getData(usersUrl, User[].class);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void testGetDataHttpClientError() {
        // Given
        when(restTemplate.getForEntity(usersUrl, User[].class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // When
        ResponseEntity<List<User>> result = fakeStoreService.getData(usersUrl, User[].class);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }


    @Test(expected = RuntimeException.class)
    public void testGetCategoryValues_Error() {
        // Arrange
        when(restTemplate.getForEntity(productsUrl, Product[].class)).thenThrow(new RestClientException("Error"));

        // Act
        fakeStoreService.getCategoryValues();
    }


    @Test
    public void testGetCategoryValues() {

        ResponseEntity<Product[]> mockResponse = ResponseEntity.ok(new Product[]{
                new Product(1, "Product 1", 10.0, "Category 1", "Description 1", "Image 1", new Rating(4.0, 10)),
                new Product(2, "Product 2", 20.0, "Category 2", "Description 2", "Image 2", new Rating(3.5, 20)),
                new Product(3, "Product 3", 30.0, "Category 1", "Description 3", "Image 3", new Rating(4.5, 30))
        });

        FakeStoreService fakeStoreService = new FakeStoreService(restTemplate, "usersUrl", "cartsUrl", "productsUrl");

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(mockResponse);

        Map<String, BigDecimal> categoryValues = fakeStoreService.getCategoryValues();
        assertEquals(BigDecimal.valueOf(40.0), categoryValues.get("Category 1"));
        assertEquals(BigDecimal.valueOf(20.0), categoryValues.get("Category 2"));
    }

}



