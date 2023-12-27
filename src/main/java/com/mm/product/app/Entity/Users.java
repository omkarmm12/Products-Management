package com.mm.product.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Column(nullable = false)
    @NotBlank(message = "username is not be blank")
    @NotNull(message = "username is not be null")
    private String username;
    @Id
    @Column(nullable = false)
    @Email(message = "invalid email address")
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "password should not be blank")
    @NotNull(message = "password should not be null")
    private String password;
    @NotBlank(message = "roles is not be blank")
    @NotNull(message = "roles is not be null")
    private String roles;

}
