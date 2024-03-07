package com.fbd.mongo;

import com.fbd.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "fbd_groups", path = "fbd_groups")
public interface MongoGroupRepository extends MongoRepository<Group, String> {
}
