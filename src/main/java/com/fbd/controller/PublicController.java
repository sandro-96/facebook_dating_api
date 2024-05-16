package com.fbd.controller;

import com.fbd.dto.LoginDTO;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import com.fbd.provider.JwtTokenProvider;
import com.fbd.service.UserApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class PublicController {
    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserApiServiceImpl userApiService;

    @GetMapping("/getUserFromToken")
    public ResponseEntity<LoginDTO> getUserFromToken(@RequestParam() String token) {
        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<User> user = mongoUserRepository.findById(userId);
        List<User> userList =  userApiService.likedList(userId);
        return ResponseEntity.ok(LoginDTO.builder()
                .userLikedCount(userList.size())
                .user(user.get())
                .jwtToken(token)
                .build());
    }
}