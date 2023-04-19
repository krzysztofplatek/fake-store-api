package com.fakestoreapi.model.carts;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Cart {

    private int id;
    private int userId;
    private Date date;
    private Products[] products;

}
