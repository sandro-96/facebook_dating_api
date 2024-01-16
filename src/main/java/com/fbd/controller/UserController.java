package com.fbd.controller;

import com.fbd.dto.LoginDTO;
import com.fbd.model.User;
import com.fbd.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @GetMapping("/checkUserName")
    public Boolean checkUserName(@RequestParam String userName) {
        return userService.checkUserName(userName);
    }
}
