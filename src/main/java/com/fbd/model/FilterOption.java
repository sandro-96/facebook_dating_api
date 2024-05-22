package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "filterOption")
public class FilterOption {
    @Id
    private String id;
    private String userId;
    private String gender;
    @Builder.Default
    private int minAge = 0;
    @Builder.Default
    private int maxAge = 60;
    @Builder.Default
    private int distance = 30;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @JsonProperty("key")
    public String getKey() {
        return id;
    }
}
