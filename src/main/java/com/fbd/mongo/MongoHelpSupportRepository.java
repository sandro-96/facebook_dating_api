package com.fbd.mongo;

import com.fbd.model.HelpSupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_helpSupports", path = "fbd_helpSupports")
public interface MongoHelpSupportRepository extends MongoRepository<HelpSupport, String> {
    List<HelpSupport> findAllByCreatedBy(String userId);
}
