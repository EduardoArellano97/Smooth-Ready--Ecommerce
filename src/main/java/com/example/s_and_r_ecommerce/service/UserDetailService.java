package com.example.s_and_r_ecommerce.service;

import com.example.s_and_r_ecommerce.model.User;
import com.example.s_and_r_ecommerce.repository.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService {
    @Autowired
    IUserService iUserService;
    @Autowired
    HttpSession httpSession;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    private UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        Optional<User> optionalUser = iUserService.findByEmail(username);
        if (optionalUser.isPresent()) {
            logger.info("User's id: {}", optionalUser.get().getId());
            httpSession.setAttribute("userID", optionalUser.get().getId());
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails
                    .User
                    .builder()
                    .username(user.getName())
                    .password(user.getPassword())
                    .roles(user.getType())
                    .build();
        }else {
            throw new UsernameNotFoundException("user not found");
        }
    }

}
