package com.fbd.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageDto {
    private String topicId;
    private String forUserId;
    private String content;
}
