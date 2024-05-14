package com.fbd.controller;

import com.fbd.dto.ChatForm;
import com.fbd.model.ChatMessage;
import com.fbd.model.PublicChat;
import com.fbd.mongo.MongoPublicChatRepository;
import com.fbd.service.ChatServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
public class ChatController {
    private final ChatServiceImpl chatService;


    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/messages/{topicId}")
    public Page<ChatMessage> getAllMessagesByTopicId(
                                                    @PathVariable String topicId,
                                                     @AuthenticationPrincipal UserDetails user,
                                                     @RequestParam(required = false) String page,
                                                     @RequestParam(required = false) String size) {
        return chatService.getMessagesByTopicId(Pageable.ofSize(Integer.parseInt(size)).withPage(Integer.parseInt(page)), topicId, user.getUsername());
    }

    @PostMapping(value = "/chat", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ChatMessage send(@RequestPart(value = "file", required = false) MultipartFile file,
                     @RequestParam("forUserId") String forUserId,
                     @RequestParam("topicId") String topicId,
                     @RequestParam("content") String content) {
        ChatForm chatForm = new ChatForm();
        chatForm.setFile(file);
        chatForm.setForUserId(forUserId);
        chatForm.setTopicId(topicId);
        chatForm.setContent(content);
        return chatService.sendMessage(chatForm);
    }

    @GetMapping("/chat/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName, @RequestParam String topicId) {
        Resource resource = chatService.getImage(imageName, topicId);
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping(value = "/chat/public/send")
    public PublicChat chatPublic(@RequestParam("content") String content, @AuthenticationPrincipal UserDetails user) {
        return chatService.chatPublic(content, user.getUsername());
    }

    @GetMapping("/chat/public")
    public Page<PublicChat> getAllPublicChats(@RequestParam(required = false) String page,
                                              @RequestParam(required = false) String size) {

        return chatService.getAllPublicChats(Pageable.ofSize(Integer.parseInt(size)).withPage(Integer.parseInt(page)));
    }

    @PutMapping("/chat/message/{messageId}/emoji")
    public ChatMessage updateEmoji(@PathVariable String messageId, @RequestBody Map<String, String> body) {
        Integer emoji = Integer.valueOf(body.get("emoji"));
        return chatService.updateEmoji(messageId, emoji);
    }
}