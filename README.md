## Introduction

This application takes data from [_Fake Store API_](https://fakestoreapi.com/) and then performs some operations based on it.

## Setup

1. **Clone the repository**
    ```bash
     git clone https://github.com/krzysztofplatek/fake-store-api.git
   ```

2. **Run the Spring Boot app**

   ```bash
   mvn spring-boot:run
   ```

   The server will start on port `8080`.
   
## Endpoints

||||
| --- | --- | --- |
| GET | /api/users | To retrieve user data |
| GET | /api/carts | To retrieve shopping cart data |
| GET | /api/products | To retrieve product data |
| GET | /api/categories-and-values | To retrieve a map containing all available product categories and the total value of products of a given category |
| GET | /api/highest-value-cart | To find a cart with the highest value, determines its value and full name of its owner |
| GET | /api/furthest-neighbors | To find the two users living furthest away from each other |

## Technologies used

* Java
* Spring Boot
* Lombok

