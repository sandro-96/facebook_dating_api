package com.fbd.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@Document(collection = "chat")
public class ChatMessage {
    private String forUserId;
    private String topicId;
    private String content;
    private byte[] image;
    @CreatedDate
    @Indexed
    private Date createdAt;
    @CreatedBy
    private String createdBy;
}