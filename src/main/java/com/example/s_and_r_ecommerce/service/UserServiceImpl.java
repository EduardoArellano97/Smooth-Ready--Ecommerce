package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  IUserService{
    @Autowired
    IUserRepository iUserRepository;
    @Override
    public Optional<User> findById(Long id) {
        return iUserRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return iUserRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return iUserRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return iUserRepository.findAll();
    }
}
