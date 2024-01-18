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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "user")
@Builder
public class User {
    @Id
    private String id;
    private String userName;
    private String name;
    private String gender;
    private String birthDay;
    private String location;
    private String password;
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
