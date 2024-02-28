package com.fbd.service;

import com.fbd.mongo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService {
    public UserService(UserRepository userRepository) {
    }
}
