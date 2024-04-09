package com.fbd.service;

import com.fbd.model.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate template;

    @Autowired
    public ChatServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        template.convertAndSendToUser(
                chatMessage.getForUserId(),
                "/queue/messages",
                chatMessage
        );
    }
}