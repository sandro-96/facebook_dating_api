package com.fbd.controller;

import com.fbd.dto.LoginDTO;
import com.fbd.model.UnreadTopic;
import com.fbd.model.User;
import com.fbd.mongo.MongoTopicRepository;
import com.fbd.mongo.MongoUnreadTopicRepository;
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
import java.util.stream.Collectors;

@Controller
public class PublicController {
    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private MongoUnreadTopicRepository mongoUnreadTopicRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserApiServiceImpl userApiService;

    @GetMapping("/getUserFromToken")
    public ResponseEntity<LoginDTO> getUserFromToken(@RequestParam() String token) {
        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<User> user = mongoUserRepository.findById(userId);
        List<User> userList =  userApiService.likedList(userId);
        List<String> unreadTopics = mongoUnreadTopicRepository.
                findByUserId(userId).stream().map(UnreadTopic::getTopicId).collect(Collectors.toList());
        return ResponseEntity.ok(LoginDTO.builder()
                .userLikedCount(userList.size())
                .unreadTopics(unreadTopics)
                .user(user.get())
                .jwtToken(token)
                .build());
    }
}