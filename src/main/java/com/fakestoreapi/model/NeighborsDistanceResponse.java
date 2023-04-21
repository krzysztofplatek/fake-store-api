package com.fakestoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NeighborsDistanceResponse {

    @JsonProperty("user1")
    private String user1Name;

    @JsonProperty("user2")
    private String user2Name;

    @JsonProperty("distance")
    private double distance;

}
