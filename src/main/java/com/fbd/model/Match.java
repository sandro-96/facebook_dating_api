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
@Document(collection = "match")
public class Match {
    @Id
    private String id;
    private String forUserId;
    @Builder.Default
    private Boolean isFromNearby = false;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @LastModifiedBy
    private String updatedBy;
    @CreatedBy
    private String createdBy;
    @JsonProperty("key")
    public String getKey() {
        return id;
    }
}
