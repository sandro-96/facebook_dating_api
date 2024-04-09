package com.fbd.service;

import com.fbd.model.ChatMessage;

public interface ChatService {
    void sendMessage(ChatMessage chatMessage);
}