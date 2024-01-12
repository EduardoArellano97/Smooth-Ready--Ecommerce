package com.example.s_and_r_ecommerce.repository;

import com.example.s_and_r_ecommerce.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
