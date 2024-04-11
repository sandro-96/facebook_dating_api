package com.fbd.mongo;

import com.fbd.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_topics", path = "fbd_topics")
public interface MongoTopicRepository extends MongoRepository<Topic, String> {
    @Query("{'$or': [{'user1.id': ?0}, {'user2.id': ?0}]}")
    List<Topic> findByUser1_IdOrUser2_Id(String userId);
}
