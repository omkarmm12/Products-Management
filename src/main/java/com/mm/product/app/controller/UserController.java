package com.mm.product.app.controller;

import com.mm.product.app.Entity.Users;
import com.mm.product.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> Add(@Valid @RequestBody Users user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.Add(user));
    }

    @PutMapping("/updatebyemail/{email}")
    public ResponseEntity<?> Update(@PathVariable String email, @RequestBody Users user) {
        Optional<Users> updated = userService.UpdateByEmail(email, user);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getall")
    public List<Users> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getbyusername/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByUsername(username));
    }

    @GetMapping("/getbyemail/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByEmail(email));
    }


}