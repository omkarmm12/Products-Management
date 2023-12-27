package com.mm.product.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;;
import com.mm.product.app.Entity.Users;
import com.mm.product.app.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    ObjectMapper objectMapper=new ObjectMapper();
    List<Users>userList=new ArrayList<>();
    @BeforeEach
    void setUp() {
        userList.add(0,new Users("user0","user0@gmail.com","pass0","ADMIN"));

        userList.add(0,new Users("user1","user1@gmail.com","pass1","USER"));
    }

    @AfterEach
    void tearDown() {
        userList.clear();
    }

    @Test
    void add() throws Exception {
        when(userService.Add(any(Users.class))).thenReturn(userList.get(0));
        String jsonString=objectMapper.writeValueAsString(userList.get(0));
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.post("/user/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(userList.get(0));
        assertEquals(string1,string);
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void update() throws Exception {
        when(userService.UpdateByEmail(eq("user0@gmail.com"),any(Users.class)))
                .thenReturn(Optional.ofNullable(userList.get(0)));
        String jsonString=objectMapper.writeValueAsString(userList.get(0));
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.put("/user/updatebyemail/user0@gmail.com").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        assertEquals(jsonString,string);
    }

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(userList);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user/getall").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(userList);
        assertEquals(string1,string);
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByUsername() throws Exception {
        when(userService.getByUsername("user0")).thenReturn(Optional.ofNullable(userList.get(0)));
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/user/getbyusername/user0").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(userList.get(0));
        assertEquals(string1,string);
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByEmail() throws Exception {
        when(userService.getByEmail("user0@gmail.com")).thenReturn(Optional.ofNullable(userList.get(0)));
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/user/getbyemail/user0@gmail.com").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(userList.get(0));
        assertEquals(string1,string);
    }
}