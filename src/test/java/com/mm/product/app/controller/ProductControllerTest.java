package com.mm.product.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mm.product.app.Entity.Product;
import com.mm.product.app.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductController productController;
    @MockBean
    private ProductService productService;

    List<Product>productList=new ArrayList<>();
    ObjectMapper objectMapper=new ObjectMapper();
    @BeforeEach
    void setUp() {
        productList.add(0,new Product("name0",22,4.9f));
        productList.add(1,new Product("name1",22,4.9f));
    }

    @AfterEach
    void tearDown() {
        productList.clear();
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void add() throws Exception {
        SecurityContext context= SecurityContextHolder.createEmptyContext();
        Authentication authentication=new UsernamePasswordAuthenticationToken
                ("user","password", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        Product product=new Product("name0",22,3.4f);
        when(productService.Add(any(Product.class))).thenReturn(product);
        String jsonString=objectMapper.writeValueAsString(product);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/product/add").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(product);
        assertEquals(string1,string);

    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void addAll() throws Exception {
        when(productService.AddAll(new HashSet<>(productList))).thenReturn(productList);
        String ListString=objectMapper.writeValueAsString(productList);
        mockMvc.perform(MockMvcRequestBuilders.post("/product/addall").content(ListString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());
        ResponseEntity<?> products=productController.addAll(new HashSet<>(productList));
        String productsString=objectMapper.writeValueAsString(products.getBody());
        assertEquals(ListString,productsString);

    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getAll() throws Exception {
        when(productService.getAll()).thenReturn(productList);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/product/getall"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(productList);
        assertEquals(string1,string);
        List<Product>products=productController.getAll();
        assertEquals("name1",products.get(1).getName());
    }

    @Test
    @DisplayName("getByName_Pass")
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByIdName() throws Exception {
        when(productService.getByIdName("name0")).thenReturn(Optional.ofNullable(productList.get(0)));
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/product/getbyname/name0").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String Actual=result.getResponse().getContentAsString();
        Product ActualT= objectMapper.readValue(Actual,Product.class);
        assertEquals(22,ActualT.getPrice());
    }
    @Test
    @DisplayName("getByName_Fail")
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByName_F() throws Exception {
        when(productService.getByIdName("name0")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/product/getbyname/name0"))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByNameIgnoreCase() throws Exception {
        when(productService.getByNameIgnoreCase("Name0")).thenReturn(Optional.ofNullable(productList.get(0)));
        MvcResult Result =mockMvc.perform(MockMvcRequestBuilders.get("/product/getbynameignorecase/Name0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String string= Result.getResponse().getContentAsString();
        Product Actual= objectMapper.readValue(string,Product.class);
        assertEquals(4.9f,Actual.getRating());
    }
    @Test
    @DisplayName("getByNameIgnoreCase_Fail")
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByNameIgnoreCase_F() throws Exception {
        when(productService.getByNameIgnoreCase("Name0")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/product/getbynameignorecase/Name0")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByRating() throws Exception {
        when(productService.getByRating(4.9f)).thenReturn(productList);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/product/getbyrating/4.9").accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String response=result.getResponse().getContentAsString();
        String Expect=objectMapper.writeValueAsString(productList);
        assertEquals(Expect,response);

    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByPrice() throws Exception {
        when(productService.getByPrice(22)).thenReturn(productList);
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/product/getbyprice/22"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(productList);
        assertEquals(string1,string);

    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void deleteAll() throws Exception {
        when(productService.deleteAll()).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/deleteall"))
                .andDo(print()).andExpect(status().isNoContent()).andExpect(content().string("All deleted"));
    }
    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    @DisplayName("deleteAll_Fail")
    void deleteAll_F() throws Exception {
        when(productService.deleteAll()).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/deleteall"))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void deleteByIdName() throws Exception {
        when(productService.deleteByidName("name0")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/deletebyname/name0"))
                .andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    @DisplayName("deleteByIdName_Fail")
    void deleteByIdName_F() throws Exception {
        when(productService.deleteByidName("name0")).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/deletebyname/name0"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void updateByIdName() throws Exception {
        Product newOne=new Product("name0",22,0);
        Product update=new Product("name0",22,3.4f);
        when(productService.updateByidName(eq("name0"),any(Product.class))).thenReturn(update);
        String jsonString=objectMapper.writeValueAsString(newOne);
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.put("/product/updatebyname/name0").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1=objectMapper.writeValueAsString(update);
        assertEquals(string1,string);
    }
    @Test
    @DisplayName("updateByIdName_Fail")
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void updateByIdName_F() throws Exception {
        Product update=new Product("name0",22,3.4f);
        when(productService.updateByidName(eq("name0"),any(Product.class))).thenReturn(null);
        String jsonString=objectMapper.writeValueAsString(update);
        mockMvc.perform(MockMvcRequestBuilders.put("/product/updatebyname/name0").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound()).andExpect(content().string("not found with name : name0"));
    }

    @Test
    @WithMockUser(username = "user",password = "pass", roles = "ADMIN")
    void getByRatingGraterThan() throws Exception {
        when(productService.getByRatingGraterThan(4.5f)).thenReturn(productList);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/product/getbyratinggraterthan/4.5"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String string=result.getResponse().getContentAsString();
        String string1= objectMapper.writeValueAsString(productList);
        assertEquals(string1,string);
    }
}