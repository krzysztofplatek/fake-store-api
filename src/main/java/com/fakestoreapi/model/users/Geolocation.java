package com.fakestoreapi.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Geolocation {

    private double lat;

    @JsonProperty("long")
    private double lng;

}
