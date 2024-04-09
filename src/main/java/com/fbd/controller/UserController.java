package com.fbd.controller;

import com.fbd.model.User;
import com.fbd.service.UserApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserApiServiceImpl userApiService;

    @GetMapping(value = "/list")
    public List<User> list(@AuthenticationPrincipal UserDetails user) {
        return userApiService.list(user.getUsername());
    }

    @GetMapping(value = "/likedList")
    public List<User> likedList(@AuthenticationPrincipal UserDetails user) {
        return userApiService.likedList(user.getUsername());
    }
}