package com.mm.product.app.Repository;

import com.mm.product.app.Entity.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    List<Users>userList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        userList.addAll(Arrays.asList(new Users("user1","gmail1@gmail.com","pass1","ADMIN")
                ,new Users("user2","gmail2@gmail.com","pass2","USER")));
        usersRepository.saveAll(userList);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByUsername() {
        Optional<Users>user1=usersRepository.findByUsername("user2");
        assertEquals("pass2",user1.get().getPassword());
        assertThat(user1.get().getRoles()).isEqualTo("USER");

    }

    @Test
    void findByEmail() {
        Optional<Users> user1= usersRepository.findByEmail("gmail1@gmail.com");
        assertEquals("user1",user1.get().getUsername());
    }
}