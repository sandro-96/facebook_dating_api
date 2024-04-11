package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.model.Match;
import com.fbd.model.User;
import com.fbd.mongo.MongoFilterOptionRepository;
import com.fbd.mongo.MongoMatchRepository;
import com.fbd.mongo.MongoTopicRepository;
import com.fbd.mongo.MongoUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserApiServiceImpl implements UserApiService {
    @Autowired
    @Lazy
    private final MongoUserRepository mongoUserRepository;
    @Autowired
    @Lazy
    private final MongoFilterOptionRepository mongoFilterOptionRepository;
    @Autowired
    @Lazy
    private final MongoMatchRepository mongoMatchRepository;
    @Autowired
    @Lazy
    private final MongoTopicRepository mongoTopicRepository;

    public UserApiServiceImpl(MongoUserRepository mongoUserRepository, MongoFilterOptionRepository mongoFilterOptionRepository, MongoMatchRepository mongoMatchRepository, MongoTopicRepository mongoTopicRepository) {
        this.mongoUserRepository = mongoUserRepository;
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
        this.mongoMatchRepository = mongoMatchRepository;
        this.mongoTopicRepository = mongoTopicRepository;
    }

    @Override
    public List<User> list(String userId) {
        List<String> likedUsers = mongoMatchRepository.findAllByCreatedBy(userId).stream().map(Match::getForUserId).collect(Collectors.toList());
        mongoTopicRepository.findByUser1_IdOrUser2_Id(userId).forEach(topic -> {
            if (topic.getUser1().getId().equals(userId)) likedUsers.add(topic.getUser2().getId());
            else likedUsers.add(topic.getUser1().getId());
        });
        List<User> list;
        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        if (option.isPresent() && option.get().getGender() != null) list = mongoUserRepository.findAllByGender(option.get().getGender());
        else list = mongoUserRepository.findAll();
        list = list.stream().filter(user1 -> !user1.getId().equals(userId) && !likedUsers.contains(user1.getId())).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<User> likedList(String userId) {
        List<String> likedUsers = mongoMatchRepository.findAllByForUserId(userId).stream().map(Match::getCreatedBy).collect(Collectors.toList());
        return (List<User>) mongoUserRepository.findAllById(likedUsers);
    }
}