package com.social.social_backend.controller;

import com.social.social_backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/ws/{userId}")
    public void handleWebSocketMessage(@Payload Map<String, Object> message, 
                                     SimpMessageHeaderAccessor headerAccessor) {
        String userId = headerAccessor.getUser().getName();
        String type = (String) message.get("type");
        
        switch (type) {
            case "join_chat":
                String chatId = (String) message.get("chatId");
                if (chatId != null) {
                    webSocketService.joinChat(userId, chatId);
                }
                break;
            case "leave_chat":
                String leaveChatId = (String) message.get("chatId");
                if (leaveChatId != null) {
                    webSocketService.leaveChat(userId, leaveChatId);
                }
                break;
            case "ping":
                // Respond with pong to keep connection alive
                webSocketService.sendPong(userId);
                break;
        }
    }
} 