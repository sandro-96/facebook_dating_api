package com.fbd.service;

import com.fbd.dto.ChatForm;
import com.fbd.model.ChatMessage;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {

    void sendMessage(ChatForm chatForm);
}