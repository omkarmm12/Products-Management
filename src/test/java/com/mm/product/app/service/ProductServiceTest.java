package com.mm.product.app.service;

import com.mm.product.app.Entity.Product;
import com.mm.product.app.Exception.UserAlreadyExistsException;
import com.mm.product.app.Exception.UserNotFoundException;
import com.mm.product.app.Repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    List<Product> productList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        productList.add(0,new Product("name0",22,4.9f));
        productList.add(1,new Product("name1",44,4.9f));
        productList.add(2,new Product("name2",44,4.8f));
        System.out.println("after productList add : "+productList);
    }

    @AfterEach
    void tearDown() {
        productList.clear();
        System.out.println("productList after clear : "+productList);
    }

    @Test
    void addSuccess() {
        Product product = new Product("name99", 66, 4.1f);
        when(productRepository.existsById(product.getName())).thenReturn(false);
        when(productRepository.save(product)).thenReturn(product);
        productService.Add(product);
//        assertThat(product1).isNotEmpty();
        verify(productRepository,times(1)).existsById(product.getName());
        verify(productRepository, times(1)).save(product);

    }
    @Test
    void addFail(){
        Product product = new Product("name99", 66, 4.1f);
        when(productRepository.existsById(product.getName())).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);
        assertThrows(UserAlreadyExistsException.class,()->productService.Add(product));
        verify(productRepository,times(1)).existsById(product.getName());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    void addAll() {
        Set<Product>productSet=new HashSet<>(productList);
        List<Product>productList1=new ArrayList<>(productSet);
        when(productRepository.saveAll(productList1)).thenReturn(productList1);
        productService.AddAll(productSet);
        verify(productRepository,times(1)).saveAll(productList1);
    }

    @Test
    void getAll() {
        when(productRepository.findAll()).thenReturn(productList);
        List<Product>products= productService.getAll();
        assertThat(productList.get(0).getName()).isEqualTo(products.get(0).getName());
        System.out.println(productList.size());
    }

    @Test
    void getByNameIgnoreCase() {
        when(productRepository.findByNameIgnoreCase("NamE0")).thenReturn (Optional.of(productList.get(0)));
        assertThat(productService.getByNameIgnoreCase("NamE0").get().getName()).isEqualTo("name0");
        System.out.println(productList.size());
    }

    @Test
    void getByIdName() {
        when(productRepository.findByNameIgnoreCase("name0")).thenReturn(Optional.ofNullable(productList.get(0)));
        Optional<Product>optional= productService.getByIdName("name0");
        if (optional.isPresent()) {
            Product product1 = optional.get();
            assertThat(product1.getPrice()).isEqualTo(22);
        }
        System.out.println(productList.size());
    }

    @Test
    void getByRating() {
        when(productRepository.findByRating(4.9f)).thenReturn(productList);
        List<Product> products= productService.getByRating(4.9f);
        assertThat(products.get(0).getName()).isEqualTo("name0");
        System.out.println(productList.size());
    }

    @Test
    void getByPrice() {
        List<Product>products=new ArrayList<>(Arrays.asList(productList.get(1),productList.get(2)));
        when(productRepository.findByPrice(44)).thenReturn(products);
        assertThat(productService.getByPrice(44).get(0).getName()).isEqualTo("name1");
        assertThat(productService.getByPrice(44).get(1).getRating()).isEqualTo(4.8f);
        System.out.println(productList.size());
    }

    @Test
    void getByRatingGraterThan() {
        List<Product>products=new ArrayList<>(Arrays.asList(productList.get(0),productList.get(1)));
        when(productRepository.findByRatingGreaterThan(4.8f)).thenReturn(products);
        assertThat(productService.getByRatingGraterThan(4.8f).get(0).getName()).isEqualTo("name0");
        System.out.println(productList.size());
    }

    @Test
    void deleteAll1() {
        when(productRepository.findAll()).thenReturn(productList);
        productService.deleteAll();
        verify(productRepository,times(1)).deleteAll();
    }
    @Test
    void deleteAll2(){List<Product>productList1=new ArrayList<>();
        when(productRepository.findAll()).thenReturn(productList1);
        productService.deleteAll();
        verify(productRepository,times(0)).deleteAll();
    }

    @Test
    void deleteByName() {
        when(productRepository.existsById("name0")).thenReturn(true);
        productService.deleteByidName("name0");
        verify(productRepository,times(1)).deleteById("name0");
    }

    @Test
    void updateByNameTestPass() {
        Product newProduct=new Product("name00",66,0.0f);
        Product Exist=new Product("name00",55,2.5f);
        when(productRepository.findById("name00")).thenReturn(Optional.ofNullable(Exist));
        when(productRepository.save(Exist)).thenReturn(Exist);
        Product result= productService.updateByidName("name00",newProduct);
        assertEquals(66,result.getPrice());
        System.out.println(result.getRating());
        verify(productRepository,times(1)).findById("name00");
        verify(productRepository,times(1)).save(Exist);

        verifyNoMoreInteractions(productRepository);
    }
    @Test
    void updatedByNameTestFail(){
        Product Update=new Product("name",999,3.5f);

        when(productRepository.findById("name11")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->productService.updateByidName("name11", Update));

        verify(productRepository,times(1)).findById("name11");
        verify(productRepository,times(0)).save(Update);

        verifyNoMoreInteractions(productRepository);

    }
}