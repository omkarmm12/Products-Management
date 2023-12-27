package com.mm.product.app.service;

import com.mm.product.app.Entity.Users;
import com.mm.product.app.Exception.UserAlreadyExistsException;
import com.mm.product.app.Exception.UserNotFoundException;
import com.mm.product.app.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public Users Add(Users user){
        boolean existsByUsername= usersRepository.existsByUsername(user.getUsername());
        boolean existsByEmail= usersRepository.existsByEmail(user.getEmail());
        if (existsByEmail||existsByUsername){
            if (existsByUsername){
                throw new UserAlreadyExistsException("User with username : "+user.getUsername()+ " already exist.");
            }
            throw new UserAlreadyExistsException("User with email : "+user.getEmail()+ " already registered");

        }else {
            user.setPassword(passwordEncoder().encode(user.getPassword()));
            user.setRoles(user.getRoles().toUpperCase());
            usersRepository.save(user);
            return user;
        }
    }

    public Optional<Users> UpdateByEmail(String email, Users employee){
        Optional<Users> ExistByEmail = usersRepository.findByEmail(email);
        boolean ExistByUsername= usersRepository.existsByUsername(employee.getUsername());
        System.out.println(ExistByUsername);
        if (ExistByEmail.isPresent()){
            Users Existing=ExistByEmail.get();
            if (!ExistByUsername&&employee.getUsername()!=null&& !employee.getUsername().isEmpty()){
                Existing.setUsername(employee.getUsername());
            }
            if (employee.getRoles()!=null&&!employee.getRoles().isEmpty()) {
                Existing.setRoles(employee.getRoles().toUpperCase());
            }
            if (employee.getPassword()!=null&& !employee.getPassword().isEmpty()){
                Existing.setPassword(employee.getPassword());
            }
            usersRepository.save(Existing);
            return Optional.of(Existing);
        }
        throw new UserNotFoundException(email +"  was not registered yet ");
    }

    public List<Users> getAll () {
        return usersRepository.findAll();
    }

    public Optional<Users> getByUsername(String username) {
        Optional<Users> user= usersRepository.findByUsername(username);
        if (user.isPresent()){
            return Optional.of(user.get());
        }else {
            throw new UserNotFoundException("user with username : "+username +" not found");
        }
    }
    public Optional<Users> getByEmail(String email){
        Optional<Users> user= usersRepository.findByEmail(email);
        if (user.isPresent()){
            return Optional.of(user.get());
        }
        throw new UserNotFoundException("Email id : "+email +" not registered");
    }
    public Optional<Users> loadByEmail(String email){
        Optional<Users>user= usersRepository.findByEmail(email);
        if (user.isPresent()){
            return user;
        }else {
            return Optional.empty();
        }
    }

}
