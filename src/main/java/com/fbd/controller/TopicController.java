package com.fbd.controller;

import com.fbd.model.Topic;
import com.fbd.service.TopicServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicServiceImpl topicService;

    @PostMapping("/createTopic")
    public Topic createTopic(@AuthenticationPrincipal UserDetails user,
                            @RequestBody Map<String, Object> topicData) {
        return topicService.createTopic(user.getUsername(), topicData);
    }

    @GetMapping("/topicsWithLatestChat")
    public List<Topic> getTopicsWithLatestChat(@AuthenticationPrincipal UserDetails user) {
        return topicService.getTopicsWithLatestChat(user.getUsername());
    }

    @DeleteMapping("/deleteTopic/{id}")
    public void deleteTopic(@AuthenticationPrincipal UserDetails user, @PathVariable String id) {
        topicService.deleteTopic(user.getUsername(), id);
    }
}