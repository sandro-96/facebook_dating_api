package com.fbd.mongo;

import com.fbd.model.UnreadTopic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "fbd_unread_topics", path = "fbd_unread_topics")
public interface MongoUnreadTopicRepository extends MongoRepository<UnreadTopic, String> {
    void deleteByTopicIdAndUserId(String topicId, String userId);

    void deleteAllByTopicId(String userId);

    Optional<UnreadTopic> findByTopicIdAndUserId(String topicId, String userId);

    List<UnreadTopic> findByUserId(String userId);
}
