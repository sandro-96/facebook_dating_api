package com.fbd.dto;

import com.fbd.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class LoginDTO {
    User user;
    int userLikedCount = 0;
    List<String> unreadTopics = new ArrayList<>();
    String jwtToken;
}
