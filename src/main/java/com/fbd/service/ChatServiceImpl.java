package com.fbd.service;

import com.fbd.constant.Constant;
import com.fbd.dto.ChatForm;
import com.fbd.dto.SocketDto;
import com.fbd.model.ChatMessage;
import com.fbd.mongo.MongoChatRepository;
import com.fbd.mongo.MongoUnreadTopicRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate template;
    private final MongoChatRepository mongoChatRepository;
    private final MongoUnreadTopicRepository mongoUnreadTopicRepository;

    @Autowired
    public ChatServiceImpl(SimpMessagingTemplate template, MongoChatRepository mongoChatRepository, MongoUnreadTopicRepository mongoUnreadTopicRepository) {
        this.template = template;
        this.mongoChatRepository = mongoChatRepository;
        this.mongoUnreadTopicRepository = mongoUnreadTopicRepository;
    }

    @Override
    public void sendMessage(ChatForm chatForm) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setForUserId(chatForm.getForUserId());
        chatMessage.setTopicId(chatForm.getTopicId());
        chatMessage.setContent(chatForm.getContent());
        if (chatForm.getFile() != null && !chatForm.getFile().isEmpty()) {
            try {
                byte[] imageBytes = chatForm.getFile().getBytes();
                chatMessage.setImage(imageBytes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert image file to byte[]", e);
            }
        }
        mongoChatRepository.save(chatMessage);
        SocketDto socketDto = createSocketDto(chatMessage.getTopicId(), chatMessage.getForUserId(), chatMessage.getContent(), chatMessage.getCreatedBy());
        sendSocketMessage(socketDto);
    }

    public List<ChatMessage> getAllMessagesByTopicIdOrderByCreatedAt(String topicId, String currentUser) {
        mongoUnreadTopicRepository.deleteByTopicIdAndUserId(topicId, currentUser);
        return mongoChatRepository.findByTopicIdOrderByCreatedAtAsc(topicId);
    }

    private SocketDto createSocketDto(String topicId, String forUserId, String content, String createdBy) {
        SocketDto socketDto = new SocketDto();
        socketDto.setType(Constant.WebSocket.SOCKET_CHAT_UPDATE);
        socketDto.setTopicId(topicId);
        socketDto.setForUserId(forUserId);
        socketDto.setContent(content);
        socketDto.setCreatedBy(createdBy);
        return socketDto;
    }

    private void sendSocketMessage(SocketDto socketDto) {
        template.convertAndSend("/queue/messages", socketDto);
    }
}