package com.fbd.service;

import com.fbd.dto.LoginDTO;
import com.fbd.model.User;
import com.fbd.mongo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
@Log4j2
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByUserNameAndPassword(loginDTO.getUserName(), loginDTO.getPassword());
        return user.orElse(null);
    }

    public User signup(User user) {
        boolean valid = userRepository.existsByUserName(user.getUserName());
        if (!valid) {
            final byte[] authBytes = user.getPassword().getBytes(StandardCharsets.UTF_8);
            final String encoded = Base64.getEncoder().encodeToString(authBytes);
            user.setPassword(encoded);
            return userRepository.save(user);
        }
        else return null;
    }

    public Boolean checkUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }
}
