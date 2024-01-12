package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.User;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Order save(Order order);
    List<Order> findAll();
    String OrderNumberGenerator();
    List<Order> findByUser(User user);
    Optional<Order> findById(Long id);

}
