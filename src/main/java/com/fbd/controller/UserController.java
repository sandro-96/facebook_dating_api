package com.fbd.controller;
import com.fbd.model.User;
import com.fbd.service.UserApiService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/users")
public class UserController {
    @Autowired
    @Lazy
    private UserApiService userApiService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "List all company goal")
    public List<User> list(
            @RequestParam(required = false, defaultValue = "") String gender,
            @AuthenticationPrincipal UserDetails user) {
        return userApiService.list(gender, user);
    }
}
