package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findAll();

}
