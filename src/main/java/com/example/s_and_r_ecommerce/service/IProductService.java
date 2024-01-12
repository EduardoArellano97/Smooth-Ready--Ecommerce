package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product save(Product product);
    Optional <Product> get(Long id);
    List<Product> findAll(); //This way we create a list of the available products.
    void update(Product product);
    void delete(Long id);
}
