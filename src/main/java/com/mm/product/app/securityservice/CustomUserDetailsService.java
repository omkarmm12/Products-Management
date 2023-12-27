package com.mm.product.app.securityservice;

import com.mm.product.app.Entity.Users;
import com.mm.product.app.Exception.UserNotFoundException;
import com.mm.product.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Autowired
    public CustomUserDetailsService(UserService userService){
        this.userService=userService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) {
      Optional<Users> user =userService.loadByEmail(email);
      if (user.isPresent()){
          return new CustomUserDetails(user.get());
      }else {
          throw new UserNotFoundException("User not registered with email : "+email);
      }
    }
}
