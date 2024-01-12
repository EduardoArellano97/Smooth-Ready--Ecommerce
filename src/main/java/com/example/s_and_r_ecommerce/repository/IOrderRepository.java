package com.example.s_and_r_ecommerce.repository;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
}
