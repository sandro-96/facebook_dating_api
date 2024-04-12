package com.fbd.controller;

import com.fbd.model.ChatMessage;
import com.fbd.service.ChatServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {
    private final ChatServiceImpl chatService;

    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/messages/ordered/{topicId}")
    public List<ChatMessage> getAllMessagesByTopicIdOrderByCreatedAt(@PathVariable String topicId) {
        return chatService.getAllMessagesByTopicIdOrderByCreatedAt(topicId);
    }

    @PostMapping("/chat")
    public void send(@RequestBody ChatMessage chatMessage) {
        chatService.sendMessage(chatMessage);
    }
}