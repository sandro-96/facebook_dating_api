package com.fbd.listener;

import com.fbd.constant.Constant;
import com.fbd.model.ChatMessage;
import com.fbd.model.PublicChat;
import com.fbd.model.UnreadTopic;
import com.fbd.mongo.MongoUnreadTopicRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class BeforeMongoPublicChatListener extends AbstractMongoEventListener<PublicChat> {
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void onAfterSave(AfterSaveEvent<PublicChat> event) {
        PublicChat publicChat = event.getSource();
        Map<String, Object> source = new HashMap<>();
        source.put("type", Constant.WebSocket.SOCKET_PUBLIC_CHAT_NEW_MESSAGE);
        source.put("data", publicChat);
        template.convertAndSend("/queue/messages", source);
        super.onAfterSave(event);
    }
}
