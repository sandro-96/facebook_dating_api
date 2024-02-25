package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "privateMessage")
@Builder
public class PrivateMessage {

    @Id
    private String id;
    private String toUserId;
    private String fromUserId;
    private String message;
    private Boolean isRead;
    @Builder.Default
    private int role = 2;
    @JsonProperty("key")
    public String getKey() {
        return id;
    }
    @CreatedDate
    private Date createdOn;
    @LastModifiedDate
    private Date updatedOn;
}