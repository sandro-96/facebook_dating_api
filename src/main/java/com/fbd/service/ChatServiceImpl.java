package com.fbd.service;

import com.fbd.model.ChatMessage;
import com.fbd.mongo.MongoChatRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate template;
    private final MongoChatRepository mongoChatRepository;

    @Autowired
    public ChatServiceImpl(SimpMessagingTemplate template, MongoChatRepository mongoChatRepository) {
        this.template = template;
        this.mongoChatRepository = mongoChatRepository;
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        mongoChatRepository.save(chatMessage);
        template.convertAndSend(
                "/queue/messages",
                chatMessage
        );
    }

    public List<ChatMessage> getAllMessagesByTopicIdOrderByCreatedAt(String topicId) {
        return mongoChatRepository.findByTopicIdOrderByCreatedAtAsc(topicId);
    }
}