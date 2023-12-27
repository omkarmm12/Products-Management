package com.mm.product.app.service;

import com.mm.product.app.Entity.Product;
import com.mm.product.app.Exception.UserAlreadyExistsException;
import com.mm.product.app.Exception.UserNotFoundException;
import com.mm.product.app.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product Add(Product product) {
        boolean Existing=productRepository.existsById(product.getName());
        if (Existing){
            throw  new UserAlreadyExistsException("product with name : "+product.getName()+ " already exist.");
        }else{
            return productRepository.save(product);
        }

    }

    public List<Product> AddAll(Set<Product> products) {
//
        List<Product>productList1= new ArrayList<>(products);
        productRepository.saveAll(productList1);
        return productList1;
    }
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getByNameIgnoreCase(String name) {
        Optional<Product> product= productRepository.findByNameIgnoreCase(name);
        if (product.isPresent()){
            return Optional.of(product.get());
        }
        return Optional.empty();
    }

    public Optional<Product> getByIdName(String name) {
        Optional<Product>product= productRepository.findById(name);
        if (product.isPresent()){
            return Optional.of(product.get());
        }
        return Optional.empty();
    }


    public List<Product> getByRating(float rating) {
        return productRepository.findByRating(rating);
    }

    public List<Product> getByPrice(double price) {
        return productRepository.findByPrice(price);
    }
    public List<Product> getByRatingGraterThan(float rating){
        return productRepository.findByRatingGreaterThan(rating);
    }

    public boolean deleteAll() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return false;
        } else {
            productRepository.deleteAll();
            return true;
        }
    }

    public boolean deleteByidName(String name) {
        boolean product = productRepository.existsById(name);
        if (product) {
            productRepository.deleteById(name);
            return true;
        } else {
            return false;
        }
    }


    public Product updateByidName(String name, Product product) {

        Optional<Product> ExistingProduct = productRepository.findById(name);

        if (ExistingProduct.isPresent()) {
            Product Exist = ExistingProduct.get();

            if (product.getPrice() != 0.0d) {
                Exist.setPrice(product.getPrice());
            }
            if (product.getRating() != 0.0f) {
                Exist.setRating(product.getRating());
            }
            productRepository.save(Exist);
            return Exist;
        } else {
            throw  new UserNotFoundException("product not found with name : " +name);

        }
    }

}
