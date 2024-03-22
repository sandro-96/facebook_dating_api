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
public class MatchService {
    @Autowired
    @Lazy
    private final MongoFilterOptionRepository mongoFilterOptionRepository;

    @Autowired
    @Lazy
    private final MongoMatchRepository mongoMatchRepository;

    public MatchService(MongoFilterOptionRepository mongoFilterOptionRepository, MongoMatchRepository mongoMatchRepository) {
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
        this.mongoMatchRepository = mongoMatchRepository;
    }

    public Optional<FilterOption> getFilter(String userId) {
        return mongoFilterOptionRepository.findByUserId(userId);
    }
    public int getCountLiked(String userId) {
        List<Match> matches = mongoMatchRepository.findAllByCreatedByAndCreatedAtLessThan(userId, DateUtils.atStartOfDay(new Date()));
        mongoMatchRepository.deleteAll(matches);
        return mongoMatchRepository
                .countAllByCreatedByAndCreatedAtGreaterThanEqual(userId, DateUtils.atStartOfDay(new Date()));
    }
}
