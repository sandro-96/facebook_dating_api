package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "topics")
public class Topic {
    @Id
    private String id;
    private String name;
    @Builder.Default
    private Boolean isPublic = false;
    private String description;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @LastModifiedBy
    private String updatedBy;
    @CreatedBy
    private String createdBy;
    private User user1;
    private User user2;
    private String lastMessage;

    @JsonProperty("key")
    public String getKey() {
        return id;
    }

    @JsonProperty("topicName")
    public String getTopicName(String userId) {
        if (user1.getId().equals(userId)) {
            return user2.getUsername();
        } else if (user2.getId().equals(userId)) {
            return user1.getUsername();
        } else {
            return null;
        }
    }
    @JsonProperty("forUserId")
    public String getForUserId(String userId) {
        if (user1.getId().equals(userId)) {
            return user2.getId();
        } else if (user2.getId().equals(userId)) {
            return user1.getId();
        } else {
            return null;
        }
    }
}