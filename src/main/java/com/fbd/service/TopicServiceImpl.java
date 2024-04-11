package com.fbd.service;

import com.fbd.model.Topic;
import com.fbd.model.User;
import com.fbd.mongo.MongoMatchRepository;
import com.fbd.mongo.MongoTopicRepository;
import com.fbd.mongo.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    private final MongoTopicRepository mongoTopicRepository;
    private final MongoUserRepository mongoUserRepository;
    private final MongoMatchRepository mongoMatchRepository;

    @Autowired
    public TopicServiceImpl(MongoTopicRepository mongoTopicRepository, MongoUserRepository mongoUserRepository, MongoMatchRepository mongoMatchRepository) {
        this.mongoTopicRepository = mongoTopicRepository;
        this.mongoUserRepository = mongoUserRepository;
        this.mongoMatchRepository = mongoMatchRepository;
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
    public void deleteTopic(String id) {
        mongoTopicRepository.deleteById(id);
    }

    public Topic createTopic(String userId, Map<String, Object> topicData) {
        String forUserId = (String) topicData.get("forUserId");
        String description = (String) topicData.get("description");
        User user1 = getUser(userId);
        User user2 = getUser(forUserId);
        Topic topic = createNewTopic(user1, user2, description);
        Topic savedTopic = addTopic(topic);
        mongoMatchRepository.deleteByCreatedByAndForUserId(forUserId, userId);
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

    public List<Topic> getTopicsByUserId(String userId) {
        return mongoTopicRepository.findByUser1_IdOrUser2_Id(userId);
    }
}