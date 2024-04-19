package com.fbd.listener;

import com.fbd.model.ChatMessage;
import com.fbd.model.UnreadTopic;
import com.fbd.mongo.MongoUnreadTopicRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class BeforeMongoChatListener extends AbstractMongoEventListener<ChatMessage> {
    @Autowired
    private MongoUnreadTopicRepository mongoUnreadTopicRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<ChatMessage> event) {
        ChatMessage chatMessage = event.getSource();
        Optional<UnreadTopic> unreadTopicOptional = mongoUnreadTopicRepository.findByTopicIdAndUserId(chatMessage.getTopicId(), chatMessage.getForUserId());

        if (!unreadTopicOptional.isPresent()) {
            UnreadTopic newUnreadTopic = new UnreadTopic();
            newUnreadTopic.setTopicId(chatMessage.getTopicId());
            newUnreadTopic.setUserId(chatMessage.getForUserId());
            // set other fields of newUnreadTopic as needed

            mongoUnreadTopicRepository.save(newUnreadTopic);
        }

        super.onAfterSave(event);
    }
}
