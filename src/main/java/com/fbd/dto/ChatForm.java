package com.fbd.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChatForm {
    private String forUserId;
    private String topicId;
    private String content;
    private MultipartFile file;
}