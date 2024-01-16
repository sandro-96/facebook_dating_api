package com.fbd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fbd.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "group")
@Builder
public class Group {
    @Id
    private String id;
    private String name;
    @Builder.Default
    private List<String> memberIds = new ArrayList<>();
    @Builder.Default
    private GroupType type = GroupType.PUBLIC;
    private String description;
    private String createdBy;
    @JsonProperty("memberCount")
    public long getMemberCount() {
        return memberIds.size();
    }
    @JsonProperty("key")
    public String getKey() {
        return id;
    }
}
