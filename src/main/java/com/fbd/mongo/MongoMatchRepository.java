package com.fbd.mongo;

import com.fbd.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fbd_matches", path = "fbd_matches")
public interface MongoMatchRepository extends MongoRepository<Match, String> {
}
