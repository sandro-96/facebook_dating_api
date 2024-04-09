package com.fbd.controller;

import com.fbd.dto.ChatMessageDto;
import com.fbd.service.ChatServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatServiceImpl chatService;

    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public void send(@RequestBody ChatMessageDto chatMessageDto) {
        chatService.sendMessage(chatMessageDto.toChatMessage());
    }
}