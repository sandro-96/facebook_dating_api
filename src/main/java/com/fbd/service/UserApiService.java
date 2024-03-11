package com.fbd.service;

import com.fbd.model.CustomUserDetails;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserApiService {
    @Autowired
    @Lazy
    private final MongoUserRepository mongoUserRepository;

    public UserApiService(MongoUserRepository mongoUserRepository) {
        this.mongoUserRepository = mongoUserRepository;
    }

    public List<User> list(String gender, UserDetails user) {
        List<User> list;
        if (!Objects.equals(gender, "")) list = mongoUserRepository.findAllByGender(gender);
        else list = mongoUserRepository.findAll();
        return list.stream().filter(user1 -> !user1.getEmail().equals(((CustomUserDetails) user).getUser().getEmail())).collect(Collectors.toList());
    }
}
