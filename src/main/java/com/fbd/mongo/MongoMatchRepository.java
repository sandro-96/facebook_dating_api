package com.fbd.mongo;

import com.fbd.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_matches", path = "fbd_matches")
public interface MongoMatchRepository extends MongoRepository<Match, String> {
    List<Match> findAllByCreatedBy(String userId);
    List<Match> findAllByForUserId(String userId);
    void deleteByCreatedByAndForUserId(String user1Id, String user2Id);
}
