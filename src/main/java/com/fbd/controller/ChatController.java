package com.fbd.controller;

import com.fbd.dto.ChatForm;
import com.fbd.model.ChatMessage;
import com.fbd.service.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public void send(@RequestPart(value = "file", required = false) MultipartFile file,
                     @RequestParam("forUserId") String forUserId,
                     @RequestParam("topicId") String topicId,
                     @RequestParam("content") String content) {
        ChatForm chatForm = new ChatForm();
        chatForm.setFile(file);
        chatForm.setForUserId(forUserId);
        chatForm.setTopicId(topicId);
        chatForm.setContent(content);
        chatService.sendMessage(chatForm);
    }

    @GetMapping("/chat/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName, @RequestParam String topicId) {
        Resource resource = chatService.getImage(imageName, topicId);
        return ResponseEntity.ok().body(resource);
    }
}