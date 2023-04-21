package com.fakestoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartValueResponse {

    @JsonProperty("cart_id")
    private int cartId;

    @JsonProperty("owner")
    private String ownerName;

    @JsonProperty("value")
    private double value;

}
