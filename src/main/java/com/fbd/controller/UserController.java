package com.fbd.controller;

import com.fbd.model.User;
import com.fbd.mongo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public User create(@RequestBody User user) {
        log.info(user);
        return userRepository.insert(user);
    }
}
