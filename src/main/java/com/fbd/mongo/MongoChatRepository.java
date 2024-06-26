package com.fbd.mongo;

import com.fbd.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_chats", path = "fbd_chats")
public interface MongoChatRepository extends MongoRepository<ChatMessage, String> {
    ChatMessage findTopByTopicIdOrderByCreatedAtDesc(String topicId);
    Page<ChatMessage> findByTopicIdOrderByCreatedAtDesc(String topicId, Pageable pageable);
    void deleteByTopicId(String topicId);
}