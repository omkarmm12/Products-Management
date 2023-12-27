package com.mm.product.app.Repository;

import com.mm.product.app.Entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    List<Product> productList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        productList.add(0, new Product("name1", 44, 4.8f));
        productList.add(1, new Product("name2", 44, 4.9f));
        productList.add(2, new Product("name3", 45, 4.8f));
        productRepository.saveAll(productList);
    }

    @Test
    void findByRatingTest() {
        List<Product>products= productRepository.findByRating(4.8f);
        assertThat(products.get(1).getName()).isEqualTo("name3");

    }
    @Test
    void findByPriceTest(){
        List<Product>products =productRepository.findByPrice(44);
        assertThat(products.get(0).getName()).isEqualTo("name1");
    }
    @Test
    void findByNameIgnoreCaseTest(){
        Optional<Product> optional= productRepository.findByNameIgnoreCase("NAMe2");
        assertThat(optional.get().getName()).isEqualTo("name2");
    }
    @Test
    void findByRatingGraterThanTest(){
        List<Product>products= productRepository.findByRatingGreaterThan(4.8f);
        assertThat(products.get(0).getName()).isEqualTo("name2");
    }
}
