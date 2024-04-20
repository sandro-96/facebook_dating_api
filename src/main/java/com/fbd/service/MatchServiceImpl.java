package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.model.MatchTurn;
import com.fbd.mongo.MongoFilterOptionRepository;
import com.fbd.mongo.MongoMatchRepository;
import com.fbd.mongo.MongoMatchTurnRepository;
import com.fbd.utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class MatchServiceImpl implements MatchService {
    @Autowired
    @Lazy
    private final MongoFilterOptionRepository mongoFilterOptionRepository;

    @Autowired
    @Lazy
    private final MongoMatchRepository mongoMatchRepository;
    @Autowired
    @Lazy
    private final MongoMatchTurnRepository mongoMatchTurnRepository;

    public MatchServiceImpl(MongoFilterOptionRepository mongoFilterOptionRepository, MongoMatchRepository mongoMatchRepository, MongoMatchTurnRepository mongoMatchTurnRepository) {
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
        this.mongoMatchRepository = mongoMatchRepository;
        this.mongoMatchTurnRepository = mongoMatchTurnRepository;
    }

    @Override
    public Optional<FilterOption> getFilter(String userId) {
        return mongoFilterOptionRepository.findByUserId(userId);
    }

    @Override
    public int matchedCount(String userId) {
        Optional<MatchTurn> matchTurn =  mongoMatchTurnRepository.findByUserId(userId);
        return matchTurn.map(MatchTurn::getTurn).orElse(0);
    }

    public void deleteAllMatches() {
        mongoMatchRepository.deleteAll();
    }
    public void deleteAllMatchTurn() {
        mongoMatchTurnRepository.deleteAll();
    }
}