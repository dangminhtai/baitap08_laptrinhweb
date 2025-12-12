package com.baitap.demo.controller;

import com.baitap.demo.entity.User;
import com.baitap.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @QueryMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @QueryMapping
    public User getUser(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public User createUser(@Argument String fullname, @Argument String email, @Argument String password,
            @Argument String phone) {
        User user = new User();
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument String fullname, @Argument String email,
            @Argument String password, @Argument String phone) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (fullname != null)
            user.setFullname(fullname);
        if (email != null)
            user.setEmail(email);
        if (password != null)
            user.setPassword(password);
        if (phone != null)
            user.setPhone(phone);
        return userRepository.save(user);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userRepository.deleteById(id);
        return true;
    }
}
