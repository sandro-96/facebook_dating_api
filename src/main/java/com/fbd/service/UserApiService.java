package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.model.User;
import com.fbd.mongo.MongoFilterOptionRepository;
import com.fbd.mongo.MongoUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserApiService {
    @Autowired
    @Lazy
    private final MongoUserRepository mongoUserRepository;
    @Autowired
    @Lazy
    private final MongoFilterOptionRepository mongoFilterOptionRepository;

    public UserApiService(MongoUserRepository mongoUserRepository, MongoFilterOptionRepository mongoFilterOptionRepository) {
        this.mongoUserRepository = mongoUserRepository;
        this.mongoFilterOptionRepository = mongoFilterOptionRepository;
    }

    public List<User> list(String userId) {
        List<User> list;
        Optional<FilterOption> option = mongoFilterOptionRepository.findByUserId(userId);
        if (option.isPresent()) list = mongoUserRepository.findAllByGender(option.get().getGender());
        else list = mongoUserRepository.findAll();
        return list.stream().filter(user1 -> !user1.getId().equals(userId)).collect(Collectors.toList());
    }
}
