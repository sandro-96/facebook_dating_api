package com.fbd.mongo;

import com.fbd.model.FilterOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "fbd_filterOptions", path = "fbd_filterOptions")
public interface MongoFilterOptionRepository extends MongoRepository<FilterOption, String> {
    Optional<FilterOption> findByUserId(String userId);
}
