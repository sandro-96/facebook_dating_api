package com.fbd.controller;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import com.fbd.service.UserApiService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/users")
public class UserController {
    @Autowired
    @Lazy
    private UserApiService userApiService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "List all users")
    public List<User> list(
            @AuthenticationPrincipal UserDetails user) {
        return userApiService.list(user.getUsername());
    }

    /*@PostMapping(value = "/generate")
    @ApiOperation(value = "List all users")
    public void generate(
            @RequestBody List<User> users,
            @AuthenticationPrincipal UserDetails user) {
        mongoUserRepository.saveAll(users);
        log.info(users);
    }*/
}
