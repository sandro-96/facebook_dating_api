package com.fbd.service;

import com.fbd.constant.Constant;
import com.fbd.dto.SocketDto;
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
        SocketDto socketDto = createSocketDto(chatMessage.getTopicId(), chatMessage.getForUserId(), chatMessage.getContent(), chatMessage.getCreatedBy());
        sendSocketMessage(socketDto);
    }

    public List<ChatMessage> getAllMessagesByTopicIdOrderByCreatedAt(String topicId) {
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