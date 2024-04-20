package com.fbd.listener;

import com.fbd.constant.Constant;
import com.fbd.dto.SocketDto;
import com.fbd.model.Match;
import com.fbd.model.MatchTurn;
import com.fbd.mongo.MongoMatchTurnRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class BeforeMongoMatchListener extends AbstractMongoEventListener<Match> {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MongoMatchTurnRepository mongoMatchTurnRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Match> event) {
        Match match = event.getSource();
        Optional<MatchTurn> matchTurnOptional = mongoMatchTurnRepository.findByUserId(match.getCreatedBy());
        MatchTurn matchTurn;
        if (matchTurnOptional.isPresent()) {
            matchTurn = matchTurnOptional.get();
            matchTurn.setTurn(matchTurn.getTurn() + 1);
        } else {
            matchTurn = MatchTurn.builder().userId(match.getCreatedBy()).turn(1).build();
        }
        mongoMatchTurnRepository.save(matchTurn);
        SocketDto socketDto = new SocketDto();
        socketDto.setType(Constant.WebSocket.SOCKET_TOPIC_UPDATE);
        socketDto.setForUserId(match.getForUserId());
        template.convertAndSend("/queue/messages", socketDto);
        super.onAfterSave(event);
    }
}
