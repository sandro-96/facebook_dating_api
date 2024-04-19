package com.fbd.controller;

import com.fbd.dto.ChatForm;
import com.fbd.model.ChatMessage;
import com.fbd.service.ChatServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatController {
    private final ChatServiceImpl chatService;

    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/messages/ordered/{topicId}")
    public List<ChatMessage> getAllMessagesByTopicIdOrderByCreatedAt(@PathVariable String topicId, @AuthenticationPrincipal UserDetails user) {
        return chatService.getAllMessagesByTopicIdOrderByCreatedAt(topicId, user.getUsername());
    }

    @PostMapping(value = "/chat", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void send(@ModelAttribute ChatForm chatForm) {
        chatService.sendMessage(chatForm);
    }
}