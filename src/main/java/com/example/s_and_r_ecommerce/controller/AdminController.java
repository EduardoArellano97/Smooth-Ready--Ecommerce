package com.example.s_and_r_ecommerce.controller;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.Product;
import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.service.IOrderService;
import com.example.s_and_r_ecommerce.service.IProductService;
import com.example.s_and_r_ecommerce.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("")
    public String home(Model model){
        List<Product> products = iProductService.findAll();
        model.addAttribute("products",products);
        return "index_admin";
    }
    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users",iUserService.findAll());
        return "users";
    }
    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders",iOrderService.findAll());
        return "my_orders";
    }
    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Long id){
        Order order = iOrderService.findById(id).get();
        model.addAttribute("details", order.getOrderDetail());

        return "order_details";
    }
}
