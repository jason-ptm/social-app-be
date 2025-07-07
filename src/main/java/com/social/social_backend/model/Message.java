
package com.social.social_backend.model;

import java.time.LocalDateTime;

public class Message {
    private Integer messageId;
    private String senderId;
    private String receiverId;
    private Integer groupId;
    private String content;
    private LocalDateTime messageDate;

    // Getters y setters
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }

    // Helper methods for compatibility
    public Integer getId() {
        return messageId;
    }

    public void setId(Integer id) {
        this.messageId = id;
    }

    public String getSenderUserId() {
        return senderId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderId = senderUserId;
    }

    public LocalDateTime getSendDate() {
        return messageDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.messageDate = sendDate;
    }
}

