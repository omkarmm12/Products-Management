package com.mm.product.app.controller;

import com.mm.product.app.Entity.Product;
import com.mm.product.app.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> add(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body( productService.Add(product));
    }

    @PostMapping("/addall")
    public ResponseEntity<?> addAll(@Valid @RequestBody Set<Product> products) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.AddAll(products));
    }

    @GetMapping("/getall")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/getbyname/{name}")
    public ResponseEntity<?> getByIdName(@PathVariable String name) {
        Optional<Product> optional=productService.getByIdName(name);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getbynameignorecase/{name}")
    public ResponseEntity<Optional<Product> > getByNameIgnoreCase(@PathVariable String name) {
        Optional<Product> product = productService.getByNameIgnoreCase(name);
        if (product.isPresent()) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getbyrating/{rating}")
    public ResponseEntity<?> getByRating(@PathVariable float rating){
        return ResponseEntity.ok( productService.getByRating(rating));
    }
    @GetMapping("/getbyprice/{price}")
    public ResponseEntity<?> getByPrice(@PathVariable double price){
        return ResponseEntity.ok(productService.getByPrice(price));
    }
    @DeleteMapping("/deleteall")
    public ResponseEntity<?>deleteAll(){
        boolean delete=productService.deleteAll();
        if (delete) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("All deleted");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/deletebyname/{name}")
    public ResponseEntity<?> deleteByIdName(@PathVariable String name){
        boolean delete=productService.deleteByidName(name);
        if (delete){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updatebyname/{name}")
    public ResponseEntity<?> updateByIdName(@PathVariable String name,@RequestBody Product product){
        Product updated = productService.updateByidName(name,product);
        if (updated!=null){
            return ResponseEntity.ok(updated);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found with name : " +name );
        }
    }
    @GetMapping("/getbyratinggraterthan/{rating}")
    public ResponseEntity<?>getByRatingGraterThan(@PathVariable float rating){
        List<Product>products= productService.getByRatingGraterThan(rating);
        return ResponseEntity.ok(products);
    }

}
