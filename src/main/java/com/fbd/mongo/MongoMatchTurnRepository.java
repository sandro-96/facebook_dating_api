package com.fbd.mongo;

import com.fbd.model.MatchTurn;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "fbd_matchTurns", path = "fbd_matchTurns")
public interface MongoMatchTurnRepository extends MongoRepository<MatchTurn, String> {
    Optional<MatchTurn> findByUserId(String userId);
}
