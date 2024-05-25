package com.fbd.listener;

import com.fbd.model.Topic;
import com.fbd.mongo.MongoChatRepository;
import com.fbd.mongo.MongoUnreadTopicRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Log4j2
@Component
public class BeforeMongoTopicListener extends AbstractMongoEventListener<Topic> {
    @Autowired
    private MongoChatRepository mongoChatRepository;

    @Autowired
    private MongoUnreadTopicRepository mongoUnreadTopicRepository;
    @Override
    public void onAfterDelete(AfterDeleteEvent<Topic> event) {
        log.info("Topic deleted: {}", event.getSource());
        mongoChatRepository.deleteByTopicId(event.getSource().getObjectId("_id").toString());
        mongoUnreadTopicRepository.deleteAllByTopicId(event.getSource().getObjectId("_id").toString());
        String directory = "/resource/file/" + event.getSource().getObjectId("_id").toString();
        try {
            FileUtils.deleteDirectory(new File(directory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete directory", e);
        }
        super.onAfterDelete(event);
    }
}
