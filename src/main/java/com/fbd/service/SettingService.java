package com.fbd.service;

import com.fbd.model.FeedBack;
import com.fbd.model.HelpSupport;
import com.fbd.mongo.MongoFeedbackRepository;
import com.fbd.mongo.MongoHelpSupportRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class SettingService {
    @Autowired
    @Lazy
    private final MongoHelpSupportRepository mongoHelpSupportRepository;

    @Autowired
    @Lazy
    private final MongoFeedbackRepository mongoFeedbackRepository;

    public SettingService(MongoHelpSupportRepository mongoHelpSupportRepository, MongoFeedbackRepository mongoFeedbackRepository) {
        this.mongoHelpSupportRepository = mongoHelpSupportRepository;
        this.mongoFeedbackRepository = mongoFeedbackRepository;
    }

    public HelpSupport createHelpSupport(HelpSupport helpSupport) {
        return mongoHelpSupportRepository.save(helpSupport);
    }
    public List<HelpSupport> getAllHelpSupportsById(String userId) {
        return mongoHelpSupportRepository.findAllByCreatedBy(userId);
    }
    public HelpSupport getHelpSupport(String id) {
        return mongoHelpSupportRepository.findById(id).orElse(null);
    }
    public HelpSupport answerHelpSupport(String answer, String id) {
        HelpSupport helpSupport = mongoHelpSupportRepository.findById(id).orElse(null);
        assert helpSupport != null;
        helpSupport.setAnswer(answer);
        return mongoHelpSupportRepository.save(helpSupport);
    }

    public FeedBack createFeedback(FeedBack feedBack) {
        return mongoFeedbackRepository.save(feedBack);
    }
    public List<FeedBack> getAllFeedbackById(String userId) {
        return mongoFeedbackRepository.findAllByCreatedBy(userId);
    }
}