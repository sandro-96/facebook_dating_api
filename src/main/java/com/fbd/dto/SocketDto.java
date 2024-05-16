package com.fbd.dto;

import com.fbd.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class SocketDto {
    private String id;
    private String topicId;
    private String forUserId;
    private String content;
    private String imagePath;
    private String type;
    private String createdBy;
    private ChatMessage lastMessage;
    private int emoji = 0;
    private Date createdAt;
}
