package com.fbd.service;

import com.fbd.model.Topic;

import java.util.List;
import java.util.Optional;

public interface TopicService {

    List<Topic> getAllTopics();

    Optional<Topic> getTopic(String id);

    Topic addTopic(Topic topic);

    Topic updateTopic(Topic topic);

    void deleteTopic(String userId, String id);
}