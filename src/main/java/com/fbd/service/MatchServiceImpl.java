package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.model.Match;
import com.fbd.mongo.MongoFilterOptionRepository;
import com.fbd.mongo.MongoMatchRepository;
import com.fbd.utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    public MatchServiceImpl(MongoFilterOptionRepository mongoFilterOptionRepository, MongoMatchRepository mongoMatchRepository) {
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
        this.mongoMatchRepository = mongoMatchRepository;
    }

    @Override
    public Optional<FilterOption> getFilter(String userId) {
        return mongoFilterOptionRepository.findByUserId(userId);
    }

    @Override
    public int getCountLiked(String userId) {
        return mongoMatchRepository
                .countAllByCreatedByAndCreatedAtGreaterThanEqual(userId, DateUtils.atStartOfDay(new Date()));
    }

    public void deleteAllMatches() {
        mongoMatchRepository.deleteAll();
    }
}