package com.fbd.dto;

import com.fbd.model.User;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginDTO {
    User user;
    int userLikedCount = 0;
    String jwtToken;
}
