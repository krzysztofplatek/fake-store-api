package com.fakestoreapi.model.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private Geolocation geolocation;

    private String city;
    private String street;
    private int number;
    private String zipcode;

}
