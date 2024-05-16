package com.fbd.service;

import com.fbd.constant.Constant;
import com.fbd.dto.ChatForm;
import com.fbd.dto.SocketDto;
import com.fbd.model.ChatMessage;
import com.fbd.model.PublicChat;
import com.fbd.model.User;
import com.fbd.mongo.MongoChatRepository;
import com.fbd.mongo.MongoPublicChatRepository;
import com.fbd.mongo.MongoUnreadTopicRepository;
import com.fbd.mongo.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {
    private final SimpMessagingTemplate template;
    private final MongoChatRepository mongoChatRepository;
    private final MongoUnreadTopicRepository mongoUnreadTopicRepository;
    private final MongoPublicChatRepository mongoPublicChatRepository;
    private final MongoUserRepository mongoUserRepository;

    @Autowired
    public ChatServiceImpl(SimpMessagingTemplate template, MongoChatRepository mongoChatRepository, MongoUnreadTopicRepository mongoUnreadTopicRepository, MongoPublicChatRepository mongoPublicChatRepository, MongoUserRepository mongoUserRepository) {
        this.template = template;
        this.mongoChatRepository = mongoChatRepository;
        this.mongoUnreadTopicRepository = mongoUnreadTopicRepository;
        this.mongoPublicChatRepository = mongoPublicChatRepository;
        this.mongoUserRepository = mongoUserRepository;
    }

    @Override
    public ChatMessage sendMessage(ChatForm chatForm) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setForUserId(chatForm.getForUserId());
        chatMessage.setTopicId(chatForm.getTopicId());
        chatMessage.setContent(chatForm.getContent());
        if (chatForm.getFile() != null && !chatForm.getFile().isEmpty()) {
            try {
                // Define the directory where you want to save the images
                String directory = "/resource/file/" + chatForm.getTopicId();
                Path dirPath = Paths.get(directory);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                Path filePath = dirPath.resolve(Objects.requireNonNull(chatForm.getFile().getOriginalFilename()));
                Files.write(filePath, chatForm.getFile().getBytes());

                // Save the path to the image in the database instead of the image itself
                MultipartFile multipartFile = chatForm.getFile();
                chatMessage.setImagePath(multipartFile.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store image file", e);
            }
        }
        ChatMessage message = mongoChatRepository.save(chatMessage);
        SocketDto socketDto = createSocketDto(message);
        sendSocketMessage(socketDto);
        return  message;
    }

    public Page<PublicChat> getAllPublicChats(Pageable pageable) {
        Page<PublicChat> messagesPage = mongoPublicChatRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<PublicChat> messages = new ArrayList<>(messagesPage.getContent());
        Collections.reverse(messages);
        Page<PublicChat> reversedMessagesPage = new PageImpl<>(messages, pageable, messagesPage.getTotalElements());
        return reversedMessagesPage;
    }

    public PublicChat chatPublic(String content, String userId) {
        User user = mongoUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setEmail(null);
        PublicChat publicChat = new PublicChat();
        publicChat.setContent(content);
        publicChat.setUserInfo(user);

        return mongoPublicChatRepository.save(publicChat);
    }

    public Resource getImage(String imageName, String topicId) {
        try {
            Path imagePath = Paths.get("/resource/file/" + topicId, imageName);
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Page<ChatMessage> getMessagesByTopicId(Pageable pageable, String topicId, String currentUser) {
        mongoUnreadTopicRepository.deleteByTopicIdAndUserId(topicId, currentUser);
        Page<ChatMessage> messagesPage = mongoChatRepository.findByTopicIdOrderByCreatedAtDesc(topicId, pageable);
        List<ChatMessage> messages = new ArrayList<>(messagesPage.getContent());
        Collections.reverse(messages);
        Page<ChatMessage> reversedMessagesPage = new PageImpl<>(messages, pageable, messagesPage.getTotalElements());
        return reversedMessagesPage;
    }

    private SocketDto createSocketDto(ChatMessage chatMessage) {
        SocketDto socketDto = new SocketDto();
        socketDto.setId(chatMessage.getId());
        socketDto.setType(Constant.WebSocket.SOCKET_CHAT_UPDATE);
        socketDto.setLastMessage(chatMessage);
        socketDto.setTopicId(chatMessage.getTopicId());
        socketDto.setForUserId(chatMessage.getForUserId());
        socketDto.setContent(chatMessage.getContent());
        socketDto.setCreatedBy(chatMessage.getCreatedBy());
        socketDto.setImagePath(chatMessage.getImagePath());
        socketDto.setCreatedAt(chatMessage.getCreatedAt());
        socketDto.setEmoji(chatMessage.getEmoji());
        return socketDto;
    }

    public ChatMessage updateEmoji(String messageId, Integer emoji) {
        Optional<ChatMessage> optionalChatMessage = mongoChatRepository.findById(messageId);
        if (optionalChatMessage.isPresent()) {
            ChatMessage chatMessage = optionalChatMessage.get();
            chatMessage.setEmoji(emoji);
            ChatMessage message = mongoChatRepository.save(chatMessage);

            Map<String, Object> source = new HashMap<>();
            source.put("type", Constant.WebSocket.SOCKET_CHAT_EMOJI);
            source.put("data", message);
            template.convertAndSend("/queue/messages", source);

            return message;
        } else {
            throw new RuntimeException("Message not found with id " + messageId);
        }
    }

    private void sendSocketMessage(SocketDto socketDto) {
        template.convertAndSend("/queue/messages", socketDto);
    }
}