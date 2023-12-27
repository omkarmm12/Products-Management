package com.mm.product.app.securityservice;

import com.mm.product.app.Entity.Users;
import com.mm.product.app.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private UserService userService;

    List<Users> userList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        userList.add(0,new Users("name0","name0@gmail.com","pass0","ADMIN"));
        userList.add(1,new Users("name1","name1@gmail.com","pass1","USER"));
        System.out.println("userList after add : "+userList);
    }

    @AfterEach
    void tearDown() {
        userList.clear();
    }

    @Test
    void loadUserByUsername() {
        Users user= new Users("name0","name0@gmail.com","pass0","ADMIN");
        Set<GrantedAuthority> ADMIN=new HashSet<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRoles().toUpperCase())));
        when(userService.loadByEmail("name0@gmail.com")).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("name0@gmail.com");
        assertEquals(ADMIN,userDetails.getAuthorities());
        assertEquals("name0@gmail.com",userDetails.getUsername());
        verify(userService,times(1)).loadByEmail("name0@gmail.com");

    }
}