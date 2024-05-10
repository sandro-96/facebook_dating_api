package com.fbd.mongo;

import com.fbd.model.FeedBack;
import com.fbd.model.HelpSupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "fbd_feedbacks", path = "fbd_feedbacks")
public interface MongoFeedbackRepository extends MongoRepository<FeedBack, String> {
    List<FeedBack> findAllByCreatedBy(String userId);
}
