package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.mongo.MongoFilterOptionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class MatchService {
    @Autowired
    @Lazy
    private final MongoFilterOptionRepository mongoFilterOptionRepository;

    public MatchService(MongoFilterOptionRepository mongoFilterOptionRepository) {
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
    }

    public Optional<FilterOption> get(String userId) {
        return mongoFilterOptionRepository.findByUserId(userId);
    }
}
