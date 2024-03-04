package com.fbd.dto;

import com.fbd.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    User user;
    String JWTToken;
}
