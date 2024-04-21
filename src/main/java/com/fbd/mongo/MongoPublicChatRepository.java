package com.fbd.mongo;

import com.fbd.model.PublicChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fbd_public_chats", path = "fbd_public_chats")
public interface MongoPublicChatRepository extends MongoRepository<PublicChat, String> {
    Page<PublicChat> findAllByOrderByCreatedAtDesc(Pageable pageable);
}