package com.fbd.service;

import com.fbd.constant.Constant;
import com.fbd.dto.SocketDto;
import com.fbd.model.ChatMessage;
import com.fbd.model.Topic;
import com.fbd.model.User;
import com.fbd.mongo.MongoChatRepository;
import com.fbd.mongo.MongoMatchRepository;
import com.fbd.mongo.MongoTopicRepository;
import com.fbd.mongo.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    private final MongoTopicRepository mongoTopicRepository;
    private final MongoUserRepository mongoUserRepository;
    private final MongoMatchRepository mongoMatchRepository;
    private final MongoChatRepository mongoChatRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public TopicServiceImpl(MongoTopicRepository mongoTopicRepository, MongoUserRepository mongoUserRepository, MongoMatchRepository mongoMatchRepository, MongoChatRepository mongoChatRepository, SimpMessagingTemplate template) {
        this.mongoTopicRepository = mongoTopicRepository;
        this.mongoUserRepository = mongoUserRepository;
        this.mongoMatchRepository = mongoMatchRepository;
        this.mongoChatRepository = mongoChatRepository;
        this.template = template;
    }

    @Override
    public List<Topic> getAllTopics() {
        return mongoTopicRepository.findAll();
    }

    @Override
    public Optional<Topic> getTopic(String id) {
        return mongoTopicRepository.findById(id);
    }

    @Override
    public Topic addTopic(Topic topic) {
        return mongoTopicRepository.save(topic);
    }

    @Override
    public Topic updateTopic(Topic topic) {
        return mongoTopicRepository.save(topic);
    }

    @Override
    public void deleteTopic(String userId, String id) {
        Optional<Topic> topic = mongoTopicRepository.findById(id);
        topic.ifPresent(value -> {
            mongoTopicRepository.delete(value);
            SocketDto socketDto = new SocketDto();
            socketDto.setType(Constant.WebSocket.SOCKET_TOPIC_DELETE);
            socketDto.setTopicId(value.getId());
            socketDto.setForUserId(value.getForUserId(userId));
            template.convertAndSend("/queue/messages", socketDto);
        });
    }

    public Topic createTopic(String userId, Map<String, Object> topicData) {
        String forUserId = (String) topicData.get("forUserId");
        String description = (String) topicData.get("description");
        User user1 = getUser(userId);
        User user2 = getUser(forUserId);
        Topic topic = createNewTopic(user1, user2, description);
        Topic savedTopic = addTopic(topic);
        mongoMatchRepository.deleteByCreatedByAndForUserId(forUserId, userId);
        SocketDto socketDto = createSocketDto(savedTopic.getId(), forUserId, userId);
        sendSocketMessage(socketDto);
        return savedTopic;
    }

    private User getUser(String userId) {
        return mongoUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    private Topic createNewTopic(User user1, User user2, String description) {
        Topic topic = new Topic();
        topic.setUser1(user1);
        topic.setUser2(user2);
        topic.setDescription(description);
        return topic;
    }

    public List<Topic> getTopicsWithLatestChat(String userId) {
        List<Topic> topics = getTopicsByUserId(userId);
        topics.forEach(topic -> {
            ChatMessage latestMessage = mongoChatRepository.findTopByTopicIdOrderByCreatedAtDesc(topic.getId());
            if (latestMessage != null) topic.setLastMessage(latestMessage.getContent());
            else topic.setLastMessage(topic.getDescription());
        });
        return topics;
    }
    public List<Topic> getTopicsByUserId(String userId) {
        return mongoTopicRepository.findByUser1_IdOrUser2_Id(userId);
    }

    private SocketDto createSocketDto(String topicId, String forUserId, String createdBy) {
        SocketDto socketDto = new SocketDto();
        socketDto.setType(Constant.WebSocket.SOCKET_TOPIC_UPDATE);
        socketDto.setTopicId(topicId);
        socketDto.setForUserId(forUserId);
        socketDto.setCreatedBy(createdBy);
        return socketDto;
    }

    private void sendSocketMessage(SocketDto socketDto) {
        template.convertAndSend("/queue/messages", socketDto);
    }
}