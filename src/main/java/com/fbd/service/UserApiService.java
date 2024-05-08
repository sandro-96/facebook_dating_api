package com.fbd.service;

import com.fbd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserApiService {
    Page<User> list(Pageable pageable, String userId);
    List<User> likedList(String userId);
}