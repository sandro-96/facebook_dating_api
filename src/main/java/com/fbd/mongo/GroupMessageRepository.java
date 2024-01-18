package com.fbd.mongo;

import com.fbd.model.GroupMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fbd_group_messages", path = "fbd_group_messages")
public interface GroupMessageRepository extends MongoRepository<GroupMessage, String> {
}
