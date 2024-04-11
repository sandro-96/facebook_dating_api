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
        List<String> likedUsers = getLikedUsers(userId);
        List<User> list = getUsersBasedOnFilterOption(userId);
        return filterUsers(list, userId, likedUsers);
    }

    private List<String> getLikedUsers(String userId) {
        List<String> likedUsersCreatedBy = mongoMatchRepository.findAllByCreatedBy(userId).stream()
                .map(Match::getForUserId)
                .collect(Collectors.toList());

        List<String> likedUsersForUserId = mongoMatchRepository.findAllByForUserId(userId).stream()
                .map(Match::getCreatedBy)
                .collect(Collectors.toList());

        List<String> topicUsers = mongoTopicRepository.findByUser1_IdOrUser2_Id(userId).stream()
                .map(topic -> topic.getUser1().getId().equals(userId) ? topic.getUser2().getId() : topic.getUser1().getId())
                .collect(Collectors.toList());

        likedUsersCreatedBy.addAll(likedUsersForUserId);
        likedUsersCreatedBy.addAll(topicUsers);
        return likedUsersCreatedBy;
    }

    private List<User> getUsersBasedOnFilterOption(String userId) {
        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        return option.isPresent() && option.get().getGender() != null ?
                mongoUserRepository.findAllByGender(option.get().getGender()) :
                mongoUserRepository.findAll();
    }

    private List<User> filterUsers(List<User> users, String userId, List<String> likedUsers) {
        return users.stream()
                .filter(user -> !user.getId().equals(userId) && !likedUsers.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> likedList(String userId) {
        List<String> likedUsers = mongoMatchRepository.findAllByForUserId(userId).stream().map(Match::getCreatedBy).collect(Collectors.toList());
        return (List<User>) mongoUserRepository.findAllById(likedUsers);
    }
}