package com.fbd.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SocketDto {
    private String topicId;
    private String forUserId;
    private String content;
    private String type;
}
