package com.fbd.listener;

import com.fbd.model.Topic;
import com.fbd.mongo.MongoChatRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BeforeMongoTopicListener extends AbstractMongoEventListener<Topic> {
    @Autowired
    private MongoChatRepository mongoChatRepository;
    @Override
    public void onAfterDelete(AfterDeleteEvent<Topic> event) {
        log.info("Topic deleted: {}", event.getSource());
        mongoChatRepository.deleteByTopicId(event.getSource().getObjectId("_id").toString());
        super.onAfterDelete(event);
    }
}
