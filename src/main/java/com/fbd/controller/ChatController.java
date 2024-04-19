package com.fbd.controller;

import com.fbd.dto.ChatForm;
import com.fbd.model.ChatMessage;
import com.fbd.service.ChatServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/chat/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("/resource/file", imageName);
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}