package com.fbd.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat")
public class ChatMessage {
    @Id
    private String id;
    private String forUserId;
    private String topicId;
    private String content;
    private String imagePath;
    @Builder.Default
    private int emoji = 0;
    @CreatedDate
    @Indexed
    private Date createdAt;
    @CreatedBy
    private String createdBy;
}