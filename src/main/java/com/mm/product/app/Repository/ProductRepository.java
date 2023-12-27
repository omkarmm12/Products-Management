package com.mm.product.app.Repository;

import com.mm.product.app.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Product> findByNameIgnoreCase(String name);

    List<Product> findByRating(float rating);

    List<Product> findByPrice(double price);

    List<Product> findByRatingGreaterThan(float rating);
}
