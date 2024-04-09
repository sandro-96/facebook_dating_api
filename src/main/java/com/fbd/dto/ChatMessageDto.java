package com.fbd.dto;

import com.fbd.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageDto {
    private String forUserId;
    private String content;
    // Convert to ChatMessage
    public ChatMessage toChatMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setForUserId(this.forUserId);
        chatMessage.setContent(this.content);
        // Set other fields as necessary
        return chatMessage;
    }
}
