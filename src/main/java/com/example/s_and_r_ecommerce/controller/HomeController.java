package com.example.s_and_r_ecommerce.controller;

import com.example.s_and_r_ecommerce.model.Order;
import com.example.s_and_r_ecommerce.model.OrderDetail;
import com.example.s_and_r_ecommerce.model.Product;
import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.service.IOrderDetailService;
import com.example.s_and_r_ecommerce.service.IOrderService;
import com.example.s_and_r_ecommerce.service.IProductService;
import com.example.s_and_r_ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    Order order = new Order();

    //Stores details of order
    List<OrderDetail> details = new ArrayList<>();
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IOrderDetailService iOrderDetailService;

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("")
    public String home(Model model, HttpSession httpSession){
        logger.info("Accessed home successfully: {}", httpSession.getAttribute("userID"));

        //Model allow us to set a name and a method. So we can use it to set values dynamically on the belonging view
        model.addAttribute("products", iProductService.findAll());

        //The header of the webpage is going to be adapted according to the session
        model.addAttribute("currentSession", httpSession.getAttribute("userID"));

        return "index";
    }

    @GetMapping("/products/{id}")
    public String ourProducts(@PathVariable Long id, Model model, HttpSession httpSession){
        logger.info("User requested info about the following product: {}",id);
        Optional<Product> productOptional = iProductService.get(id);
        Product product = productOptional.get();
        model.addAttribute("currentSession",httpSession.getAttribute("userID"));
        model.addAttribute("product", product);
        return "product_details";
    }

    @PostMapping("/cart")
    public String cart(@RequestParam Long id, @RequestParam Integer quantity, Model model, HttpSession httpSession){

        OrderDetail orderDetail = new OrderDetail();
        double totalSum= 0;
        Optional<Product> optionalProduct = iProductService.get(id);
        Product product= optionalProduct.get();
        logger.info("Product Added to Cart: {}", product);
        logger.info("Quantity selected: {}", quantity);

        //In here we are going to set the information according to the customer's selection
        //That way, we're going to have the orderDetail ready to be displayed.
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setName(product.getName());
        orderDetail.setTotal(product.getPrice() * quantity);
        orderDetail.setProduct(product);

        //Now let's avoid the product to be duplicated in the cart.
        Long productId = product.getId();
        Boolean existingProduct = details.stream().anyMatch(n->n.getProduct().getId()==productId);
        if(!existingProduct){
            details.add(orderDetail);
        }
        totalSum = details.stream().mapToDouble(d->d.getTotal()).sum();
        order.setTotal(totalSum);

        model.addAttribute("cart",details);
        model.addAttribute("order",order);
        model.addAttribute("currentSession", httpSession.getAttribute("userID"));
        if (httpSession.getAttribute("userID") == null){
            return "login";
        }else {
            return "cart";
        }


    }

    @GetMapping("/delete/cart/{id}")
    public String deleteFromCart(@PathVariable Long id, Model model){
        /*To delete a product from the cart, it is necessary to understand that, when we're talking about
        the cart, we're talking about a list, that way, we can UPDATE the list without the product selected to eliminate.
        */

        //For us to proceed, let's create a NEW LIST in which we're going to save the desire products
        List<OrderDetail> newOrder = new ArrayList<>();
        //We're going to iterate through the list so we can identify a different id from the selected, that way we can add it to the new list

        for (OrderDetail i: details) {
            if (i.getProduct().getId() != id){
                newOrder.add(i);
            }
        }

        //This way we make sure we add all the products except the undersired ones.
        details= newOrder;
        double totalSum = details.stream().mapToDouble(n->n.getTotal()).sum();
        order.setTotal(totalSum);

        model.addAttribute("cart",details);
        model.addAttribute("order",order);

        return "cart";
    }
    @GetMapping("/getCart")
    public String getCart(Model model,HttpSession httpSession){
        /* Since it is not possible to access from a Post-method, it is necessary to create a
         Get method, so we can map it and access to it from the home page.*/
        model.addAttribute("cart",details);
        model.addAttribute("order",order);
        model.addAttribute("currentSession",httpSession.getAttribute("userID"));
        if (httpSession.getAttribute("userID") == null){
            return "login";
        }else {
            return "cart";
        }

    }

    @GetMapping("/order")
    public String orderSummary(Model model,HttpSession httpSession){
        //We have not register users yet or include Spring Security, so we proceed to use the hardcoded one.
        User user = iUserService.findById(Long.parseLong(httpSession.getAttribute("userID").toString())).get();
        model.addAttribute("cart",details);
        model.addAttribute("order",order);
        model.addAttribute("user",user);
        model.addAttribute("currentSession", httpSession.getAttribute("userID"));

        return "order_summary";

    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession){
        // Fill data according to the order
        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        order.setNumber(iOrderService.OrderNumberGenerator());
        // Fill data according to User, Spring Scurity is not implemented yet.
        User user = iUserService.findById(Long.parseLong(httpSession.getAttribute("userID").toString())).get();
        order.setUser(user);
        // Save order
        iOrderService.save(order);
        // Save set details to "details" list
        for (OrderDetail odt: details) {
            odt.setOrder(order);
            iOrderDetailService.save(odt);
        }
        // Set changes to the list. Clear list.
        order = new Order();
        details.clear();
        return "redirect:/";
    }
    @PostMapping("/search")
    public String searchBar(@RequestParam String name, Model model){
        logger.info("The following product's been fetched: {}",name);
        List<Product> products = iProductService.findAll().stream().filter(p->p.getName().contains(name)).collect(Collectors.toList());
        model.addAttribute("products",products);
        return "index";
    }

}
