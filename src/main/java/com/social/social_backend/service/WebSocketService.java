package com.social.social_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Store active connections: {user_id: session_info}
    private final Map<String, String> activeConnections = new ConcurrentHashMap<>();
    
    // Store user sessions: {user_id: set of chat_ids}
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();

    public void connect(String userId, String sessionId) {
        activeConnections.put(userId, sessionId);
        userSessions.put(userId, ConcurrentHashMap.newKeySet());
        System.out.println("User " + userId + " connected. Total connections: " + activeConnections.size());
    }

    public void disconnect(String userId) {
        activeConnections.remove(userId);
        userSessions.remove(userId);
        System.out.println("User " + userId + " disconnected. Total connections: " + activeConnections.size());
    }

    public void sendPersonalMessage(Map<String, Object> message, String userId) {
        if (activeConnections.containsKey(userId)) {
            try {
                messagingTemplate.convertAndSendToUser(userId, "/topic/messages", message);
            } catch (Exception e) {
                System.out.println("Error sending message to user " + userId + ": " + e.getMessage());
                disconnect(userId);
            }
        }
    }

    public void broadcastMessage(Map<String, Object> message, String chatId, String excludeUser) {
        // For user-to-user chats, notify the other user
        if (!chatId.matches("\\d+")) { // Assuming group IDs are numeric
            if (!chatId.equals(excludeUser)) {
                sendPersonalMessage(message, chatId);
            }
        } else {
            // For group chats, we'll need to get group members from database
            // This will be handled in the message service
        }
    }

    public void joinChat(String userId, String chatId) {
        Set<String> userChats = userSessions.get(userId);
        if (userChats != null) {
            userChats.add(chatId);
            System.out.println("User " + userId + " joined chat " + chatId);
        }
    }

    public void leaveChat(String userId, String chatId) {
        Set<String> userChats = userSessions.get(userId);
        if (userChats != null) {
            userChats.remove(chatId);
            System.out.println("User " + userId + " left chat " + chatId);
        }
    }

    public void sendPong(String userId) {
        Map<String, Object> pongMessage = Map.of("type", "pong");
        sendPersonalMessage(pongMessage, userId);
    }

    public void broadcastNewMessage(int messageId, String senderId, String receiverId, 
                                  Integer groupId, String content, String messageType, 
                                  String timestamp, Map<String, Object> fileInfo) {
        
        // Get sender name and avatar from database
        // This would typically be done through a service call
        String senderName = senderId; // Default to ID if name not available
        String senderAvatar = ""; // Default empty avatar
        
        Map<String, Object> messageData = Map.of(
            "type", "new_message",
            "data", Map.of(
                "messageId", messageId,
                "senderId", senderId,
                "senderName", senderName,
                "senderAvatar", senderAvatar,
                "receiverId", receiverId,
                "groupId", groupId,
                "content", content,
                "messageType", messageType,
                "timestamp", timestamp,
                "fileInfo", fileInfo != null ? fileInfo : Map.of()
            )
        );

        if (groupId != null) {
            // Group message - broadcast to all group members
            // This would typically query the database for group members
            // For now, we'll just send to the receiver
            sendPersonalMessage(messageData, receiverId);
        } else {
            sendPersonalMessage(messageData, receiverId);
        }
    }
} 