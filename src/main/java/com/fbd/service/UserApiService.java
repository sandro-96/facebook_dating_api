package com.fbd.service;

import com.fbd.model.User;

import java.util.List;

public interface UserApiService {
    List<User> list(String userId);
    List<User> likedList(String userId);
}