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
@Document(collection = "publicChats")
public class PublicChat {
    private String content;
    private User userInfo;
    @CreatedDate
    @Indexed
    private Date createdAt;
    @CreatedBy
    private String createdBy;
}