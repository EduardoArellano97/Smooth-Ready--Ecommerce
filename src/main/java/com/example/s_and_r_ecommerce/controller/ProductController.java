package com.example.s_and_r_ecommerce.controller;

import com.example.s_and_r_ecommerce.model.Product;
import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.service.FileHandlerService;
import com.example.s_and_r_ecommerce.service.IProductService;
import com.example.s_and_r_ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private FileHandlerService fileHandlerService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("")
    public String products(Model model){
        model.addAttribute("products", iProductService.findAll());
        return "see_products";
    }
    @GetMapping("/create")
    public String create(){
        return "create";
    }

    @PostMapping("/save")
    public String save(Product product, @RequestParam("img") MultipartFile file, HttpSession httpSession) throws IOException {
        //product object will contain the information registered on the create form. We can print it with logger.
        logger.info("The following product has been added: {}", product);
        //as a user object is needed, hardcoding it would be a temporary solution which will be replaced later on.
        //The first step is: create a user right on the DB. Not necessary to fill all the fields
        User user = iUserService.findById(Long.parseLong(httpSession.getAttribute("userID").toString())).get();
        product.setUser(user);
        //save image implementation
        if(product.getId()==null){
            String imageName = fileHandlerService.saveImage(file);
            product.setImage(imageName);
        }
        iProductService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        /*The object's id is going to be fetch by this funtion. That's why we are going to use an Optional
        since we need to make sure that the Id actually belongs to a product.Only then could we save changes.*/

        Optional<Product> optionalProduct = iProductService.get(id);
            Product product=optionalProduct.get();
            logger.info("Fetched following product: {}", product);
            model.addAttribute("product",product);
        return "edit";
    }

    @PostMapping("/update")
    public String update(Product product, @RequestParam("img") MultipartFile file) throws IOException{
        /* It is necessary to request information from view, such as the img file, that's why
        we use the request param. */
        Product productUpdate = iProductService.get(product.getId()).get();
        if (file.isEmpty()){
            product.setImage(productUpdate.getImage()); //This way, if there is no image to update, the previous image remains
        } else{ //we make sure that the default already exist, otherwise we upload it
            if (!productUpdate.getImage().equals("default.png")){
                fileHandlerService.delete(productUpdate.getImage());
            }
            String imageName = fileHandlerService.saveImage(file);
            product.setImage(imageName);
        }
        product.setUser(productUpdate.getUser());
        iProductService.update(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Product product = iProductService.get(id).get();
        /*it is possible to use the default image so we can make sure the product we want to delete
        actually exists */
        if(!product.getImage().equals("default.png")){
            fileHandlerService.delete(product.getImage());
        }
        iProductService.delete(id);
        return "redirect:/products";
    }
}
