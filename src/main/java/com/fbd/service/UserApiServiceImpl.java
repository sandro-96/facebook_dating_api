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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
    @Autowired
    private MongoTemplate mongoTemplate;

    public UserApiServiceImpl(MongoUserRepository mongoUserRepository, MongoFilterOptionRepository mongoFilterOptionRepository, MongoMatchRepository mongoMatchRepository, MongoTopicRepository mongoTopicRepository) {
        this.mongoUserRepository = mongoUserRepository;
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
        this.mongoMatchRepository = mongoMatchRepository;
        this.mongoTopicRepository = mongoTopicRepository;
    }

    @Override
    public Page<User> list(Pageable pageable, String userId) {
        List<String> notIncludeUsers = getLikedUsers(userId);
        notIncludeUsers.add(userId);
        return getUsersBasedOnFilterOption(pageable, userId, notIncludeUsers);
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

    /*private Page<User> getUsersBasedOnFilterOption(Pageable pageable, String userId, List<String> notIncludeUsers) {

        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        if (!option.isPresent()) {
            return mongoUserRepository.findAllByIdNotIn(notIncludeUsers, pageable);
        } else {
            FilterOption filterOption = option.get();
            if (filterOption.getGender() == null) return mongoUserRepository.findAllByIdNotIn(notIncludeUsers, pageable);
            return mongoUserRepository.findAllByGenderAndIdNotIn(option.get().getGender(), notIncludeUsers , pageable);
        }
    }*/

    private Page<User> getUsersBasedOnFilterOption(Pageable pageable, String userId, List<String> notIncludeUsers) {
        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").nin(notIncludeUsers));
        if (option.isPresent()) {
            FilterOption filterOption = option.get();
            if (filterOption.getGender() != null) {
                query.addCriteria(Criteria.where("gender").is(filterOption.getGender()));
            }
            query.addCriteria(Criteria.where("age").gte(filterOption.getMinAge()).lte(filterOption.getMaxAge()));
        }
        query.with(pageable);
        List<User> users = mongoTemplate.find(query, User.class);
        return new PageImpl<>(users, pageable, mongoTemplate.count(query, User.class));
    }

    @Override
    public List<User> likedList(String userId) {
        List<Match> matches = mongoMatchRepository.findAllByForUserId(userId);
        List<String> likedUsers = matches.stream()
                .map(Match::getCreatedBy)
                .collect(Collectors.toList());

        List<String> nearByUsers = matches.stream()
                .filter(Match::getIsFromNearby)
                .map(Match::getCreatedBy)
                .collect(Collectors.toList());

        List<User> users = (List<User>) mongoUserRepository.findAllById(likedUsers);

        users.forEach(user -> user.setIsFromNearby(nearByUsers.contains(user.getId())));

        return users;
    }

    public User locationUpdate(String userId, double latitude, double longitude) {
        User user = mongoUserRepository.findById(userId).orElse(null);
        assert user != null;
        Point userLocation = new Point(longitude, latitude);
        user.setPoint(userLocation);
        return mongoUserRepository.save(user);
    }

    public Page<User> findNearbyUsers(double longitude, double latitude, String userId, Pageable pageable) {
        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        Point userLocation = new Point(longitude, latitude);
        AtomicReference<Distance> distance = new AtomicReference<>(new Distance(30, Metrics.KILOMETERS));
        Query query = new Query();
        option.ifPresent(filterOption -> {
            if (filterOption.getDistance() != 0) {
                distance.set(new Distance(filterOption.getDistance(), Metrics.KILOMETERS));
            }
            if (filterOption.getGender() != null) {
                query.addCriteria(Criteria.where("gender").is(filterOption.getGender()));
            }
        });
        query.addCriteria(Criteria.where("id").ne(userId));
        query.addCriteria(Criteria.where("point").nearSphere(userLocation).maxDistance(distance.get().getNormalizedValue()));
        long total = mongoTemplate.count(query, User.class);
        query.with(pageable);
        List<User> users = mongoTemplate.find(query, User.class);
        List<String> notIncludeUsers = getLikedUsers(userId);
        users.forEach(user -> {
            if (notIncludeUsers.contains(user.getId())) {
                user.setIsLikeDisable(true);
            }
        });
        return new PageImpl<>(users, pageable, total);
    }
}