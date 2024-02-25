package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fbd.enums.RegistrationSource;
import com.fbd.enums.UserRole;
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
    private String name;
    private String email;
    private String gender;
    private String birthDay;
    private String location;
    private String avatarUrl;
    @Builder.Default
    private UserRole role = UserRole.ROLE_USER;
    @Builder.Default
    private RegistrationSource registrationId = RegistrationSource.google;
    @JsonProperty("key")
    public String getKey() {
        return id;
    }
    @CreatedDate
    private Date createdOn;
    @LastModifiedDate
    private Date updatedOn;
}
