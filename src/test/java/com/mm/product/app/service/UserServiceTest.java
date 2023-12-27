package com.mm.product.app.service;


import com.mm.product.app.Entity.Users;
import com.mm.product.app.Exception.UserNotFoundException;
import com.mm.product.app.Repository.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.mm.product.app.Exception.UserAlreadyExistsException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UsersRepository userRepository;

    List<Users> userList = new ArrayList<>();
    @BeforeAll
    static void beforeAll(){
        System.out.println("UserServiceTest class is starting..");
    }
    @BeforeEach
    void setUp() {
        userList.add(0,new Users("name0","name0@gmail.com","pass0","ADMIN"));
        userList.add(1,new Users("name1","name1@gmail.com","pass1","USER"));
        System.out.println("userList after add : "+userList);
    }

    @AfterEach
    void tearDown() {
        userList.clear();

        System.out.println("userList after clear :  "+userList);
    }

    @Test
    @DisplayName("AddUser_Success")
    void addPass(){
        Users newUser=new Users("name1","name1@gmail.com","pass1","USER");
        when(userRepository.existsByEmail("name1@gmail.com")).thenReturn(false);
        when(userRepository.existsByUsername("name1")).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);
        Optional<Users> user= Optional.ofNullable(userService.Add(newUser));
        assertEquals("name1",user.get().getUsername());
        System.out.println(user.get().getPassword());
        verify(userRepository,times(1)).existsByUsername("name1");
        verify(userRepository,times(1)).existsByEmail("name1@gmail.com");
        verify(userRepository,times(1)).save(newUser);

    }
    @Test
    @DisplayName("AddUser_Failed")
    void addFail() {
        when(userRepository.existsByEmail("name1@gmail.com")).thenReturn(true);
        when(userRepository.existsByUsername("name1")).thenReturn(false);
        assertThrows(UserAlreadyExistsException.class,()->userService.Add(userList.get(1)));
        verify(userRepository,times(1)).existsByUsername("name1");
        verify(userRepository,times(1)).existsByEmail("name1@gmail.com");
    }

    @Test
    @DisplayName("UpdateUser_Success")
    void updateByEmailPass() {
        Users update=new Users(null,"name22@gmail.com","pass22",null);
        Users exist=new Users("name1","name1@gmail.com","pass1","USER");

        when(userRepository.findByEmail("name1@gmail.com")).thenReturn(Optional.ofNullable(exist));
        when(userRepository.existsByUsername(null)).thenReturn(false);
        when(userRepository.save(exist)).thenReturn(exist);
        Optional<Users>result=userService.UpdateByEmail("name1@gmail.com",update);
        assertEquals("name1",result.get().getUsername());

        verify(userRepository,times(1)).findByEmail("name1@gmail.com");
        verify(userRepository,times(1)).existsByUsername(null);
        verify(userRepository,times(1)).save(exist);
    }
    @Test
    @DisplayName("UpdateUser_Failed")
    void updateByEmailFail(){
        Users update=new Users(null,"name22@gmail.com","pass22",null);
        when(userRepository.findByEmail("name99@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.UpdateByEmail("name99@gmail.com",update));
        verify(userRepository,times(1)).findByEmail("name99@gmail.com");
    }

    @Test
    @DisplayName("GetAll_Users")
    void getAll() {
        when(userRepository.findAll()).thenReturn(userList);
        List<Users>users= userService.getAll();
        assertEquals("name0",users.get(0).getUsername());
        assertEquals("name1@gmail.com",users.get(1).getEmail());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("GetByUsername_Success")
    void getByUsernamePass() {
        when(userRepository.findByUsername("name1")).thenReturn(Optional.ofNullable(userList.get(1)));
        Optional<Users>user= userService.getByUsername("name1");
        assertEquals("name1@gmail.com",user.get().getEmail());
        verify(userRepository,times(1)).findByUsername("name1");
    }
    @Test
    @DisplayName("GetByUsername_Failed")
    void getByUsernameFail() {
        when(userRepository.findByUsername("name1")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.getByUsername("name1"));
        verify(userRepository,times(1)).findByUsername("name1");
    }

    @Test
    @DisplayName("GetByEmail_Success")
    void getByEmailPass() {
        when(userRepository.findByEmail("name1@gmail.com")).thenReturn(Optional.ofNullable(userList.get(1)));
        Optional<Users>user=userService.getByEmail("name1@gmail.com");
        assertEquals("name1",user.get().getUsername());
        verify(userRepository,times(1)).findByEmail("name1@gmail.com");
    }
    @Test
    @DisplayName("GetByEmail_Failed")
    void getByEmailFail(){
        when(userRepository.findByEmail("name2@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.getByEmail("name2@gmail.com"));
        verify(userRepository,times(1)).findByEmail("name2@gmail.com");
    }

    @Test
    @DisplayName("LoadByEmail_Success")
    void loadByEmailPass() {
        when(userRepository.findByEmail("name0@gmail.com")).thenReturn(Optional.ofNullable(userList.get(0)));
        Optional<Users>user=userService.loadByEmail("name0@gmail.com");
        assertEquals("name0",user.get().getUsername());
        verify(userRepository,times(1)).findByEmail("name0@gmail.com");
    }
    @Test
    @DisplayName("LoadByEmail_Failed")
    void loadByEmailFail(){
        when(userRepository.findByEmail("name9@gamil.com")).thenReturn(Optional.empty());
        Optional<Users>user=userService.loadByEmail("name9@gmail.com");
        assertNull(user.orElse(null));
        verify(userRepository,times(1)).findByEmail("name9@gmail.com");
    }
    @AfterAll
    static void afterAll(){
        System.out.println("UserServiceTest class is Ended..");
    }

}
