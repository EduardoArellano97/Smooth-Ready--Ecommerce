package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.Product;
import com.example.s_and_r_ecommerce.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private IProductRepository iProductRepository;
    @Override
    public Product save(Product product) {
        return iProductRepository.save(product);
    }

    @Override
    public Optional<Product> get(Long id) {
        return iProductRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return iProductRepository.findAll();
    }
    @Override
    public void update(Product product) {
        iProductRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        iProductRepository.deleteById(id);
    }
}
