package com.fbd.service;

import com.fbd.model.CustomUserDetails;
import com.fbd.model.User;
import com.fbd.mongo.MongoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = mongoUserRepository.findById(s);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        } else throw new UsernameNotFoundException(s);
    }
}
