package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.OrderDetail;
import com.example.s_and_r_ecommerce.repository.IOrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailImpl implements IOrderDetailService{
    @Autowired
    private IOrderDetailRepository iOrderDetailRepository;

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return iOrderDetailRepository.save(orderDetail);
    }
}
