package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    @Indexed(name = "email_user_index_unique", unique = true)
    private String email;
    private int birthYear;
    private String location;
    private String gender;
    private String bio;
    private String avatar;
    private int age;
    @Builder.Default
    private Boolean isFirstLogin = true;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @JsonProperty("key")
    public String getKey() {
        return id;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
        this.age = calculateAge(birthYear);
    }

    private int calculateAge(int birthYear) {
        int currentYear = LocalDate.now().getYear();
        return currentYear - birthYear;
    }
}
