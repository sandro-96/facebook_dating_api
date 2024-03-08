package com.fbd.controller;

import com.fbd.dto.LoginDTO;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import com.fbd.provider.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@Log4j2
public class PublicController {
    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    @Lazy
    private JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "Get User Info From Token")
    @GetMapping("/getUserFromToken")
    public ResponseEntity<LoginDTO> getUserFromToken(@RequestParam() String token) throws Exception {
        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<User> user = mongoUserRepository.findById(userId);
        return ResponseEntity.ok(LoginDTO.builder()
                .user(user.get())
                .jwtToken(token)
                .build());
    }
}
