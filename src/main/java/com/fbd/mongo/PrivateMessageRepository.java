package com.fbd.mongo;

import com.fbd.model.PrivateMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fbd_private_messages", path = "fbd_private_messages")
public interface PrivateMessageRepository extends MongoRepository<PrivateMessage, String> {
}
