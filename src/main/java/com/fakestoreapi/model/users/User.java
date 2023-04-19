package com.fakestoreapi.model.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Address address;

    private int id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;

}
