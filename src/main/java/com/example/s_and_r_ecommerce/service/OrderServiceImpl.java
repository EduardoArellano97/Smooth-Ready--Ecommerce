package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class OrderServiceImpl implements IOrderService{
    @Autowired
    private IOrderRepository iOrderRepository;
    @Override
    public Order save(Order order) {
        return iOrderRepository.save(order);
    }
    @Override
    public List<Order> findAll() {
        return iOrderRepository.findAll();
    }

    @Override
    public String OrderNumberGenerator() {
        int number;
        String numberChain ="";
        List<Order> orders = findAll();
        List<Integer> numbers = new ArrayList<>();
        orders.stream().forEach(  o -> numbers.add(Integer.parseInt(o.getNumber())));

        if (orders.isEmpty()){
            number=1;
        } else{
            number = numbers.stream().max(Integer::compare).get();
            number++;
        }
        if (number<10){
            numberChain="000000000"+String.valueOf(number);
        } else if (number<100) {
            numberChain="00000000"+String.valueOf(number);
        } else if (number<1000) {
            numberChain="0000000" +String.valueOf(number);
        } else if (number<10000){
            numberChain="000000"+String.valueOf(number);
        }
        return numberChain;
        }


    @Override
    public List<Order> findByUser(User user) {
        return iOrderRepository.findByUser(user);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return iOrderRepository.findById(id);
    }
}
