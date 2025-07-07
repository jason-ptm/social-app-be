// MessageResponse.java
package com.social.social_backend.model;

import java.util.List;

public class MessageResponse {
    private String chatType;
    private List<MessageItem> messages;
    private boolean success;
    private String message;

    // Constructor for chat messages response
    public MessageResponse(String chatType, List<MessageItem> messages) {
        this.chatType = chatType;
        this.messages = messages;
        this.success = true;
    }

    // Constructor for send message response
    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Default constructor
    public MessageResponse() {
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public List<MessageItem> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageItem> messages) {
        this.messages = messages;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class MessageItem {
        private String id;
        private String transmitter;
        private String message;
        private String sendDate;
        private boolean owner;
        private String avatar;

        public MessageItem() {
        }

        public MessageItem(String id, String transmitter, String message, String sendDate, boolean owner, String avatar) {
            this.id = id;
            this.transmitter = transmitter;
            this.message = message;
            this.sendDate = sendDate;
            this.owner = owner;
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTransmitter() {
            return transmitter;
        }

        public void setTransmitter(String transmitter) {
            this.transmitter = transmitter;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSendDate() {
            return sendDate;
        }

        public void setSendDate(String sendDate) {
            this.sendDate = sendDate;
        }

        public boolean isOwner() {
            return owner;
        }

        public void setOwner(boolean owner) {
            this.owner = owner;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}