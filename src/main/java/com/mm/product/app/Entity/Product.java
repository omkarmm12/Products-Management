package com.mm.product.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column(unique = true)
    @NotNull(message = "should not null")
    @NotBlank(message = "should not blank")
    private String name;
    @DecimalMin(value = "1.0")
    private double price;
    @DecimalMax(value = "5.0")
    @DecimalMin(value = "0.0")
    private float rating;

}
