package com.example.s_and_r_ecommerce.controller;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.service.IOrderService;
import com.example.s_and_r_ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
     IUserService iUserService;
    @Autowired
     IOrderService iOrderService;

    Logger logger = LoggerFactory.getLogger(UserController.class);
    private BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder();


    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/save")
    public String save(User user){
        Optional<User> userMail = iUserService.findByEmail(user.getEmail());
        if (userMail.isPresent()){
            logger.info("Failed registration, user already exists: {}",user);
            return "redirect:/user/register";
        }
        logger.info("Following user has been register: {}", user);
        user.setType("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        iUserService.save(user);
        return "redirect:/";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/access")
    public String access(User user, HttpSession httpSession){
        if(user.getEmail().equals("admin@admin.com")){
            return "redirect:/admin";
        }
        try{
            //Using the user's email, we can fetch the user  and get the ID
            Optional<User> userMail = iUserService.findByEmail(user.getEmail());
            logger.info("Email belongs to the following user: {}",userMail.get());

            //We are going to set the session attribute, that will help us use the session to identify if it is a user or admin.
            if (userMail.isPresent()){
                httpSession.setAttribute("userID",userMail.get().getId());
                logger.info("Logged as: "+userMail.get().getType());
                return "redirect:/";
            } else{
                logger.info("User not found");
            }
            return "redirect:/";
        }catch(Exception e){
            logger.info("A problem has ocurred {}", e);
            return  "redirect:/";
        }
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpSession httpSession){
        model.addAttribute("currentSession",httpSession.getAttribute("userID"));
        User user=iUserService.findById(Long.parseLong(httpSession.getAttribute("userID").toString())).get();
        List<Order> orders = iOrderService.findByUser(user);
        model.addAttribute("orders",orders);
        return "my_orders";
    }
    @GetMapping("/detail/{id}")
    public String odetails(Model model, @PathVariable Long id, HttpSession httpSession){
        logger.info("Session Id: {}", id);
        Order order = iOrderService.findById(id).get();
        model.addAttribute("detail", order.getOrderDetail());

        //Session attribute added
        model.addAttribute("currentSession",httpSession.getAttribute("userID"));

        return "order_details_user";
    }
    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("userID");
        return "redirect:/";
    }



}
