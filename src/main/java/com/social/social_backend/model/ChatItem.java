package com.social.social_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatItem {
    private String id;
    private String name;
    private String avatar;
    
    @JsonProperty("lastInteraction")
    private LastInteraction lastInteraction;

    public ChatItem() {
    }

    public ChatItem(String id, String name, String avatar, LastInteraction lastInteraction) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.lastInteraction = lastInteraction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LastInteraction getLastInteraction() {
        return lastInteraction;
    }

    public void setLastInteraction(LastInteraction lastInteraction) {
        this.lastInteraction = lastInteraction;
    }

    public static class LastInteraction {
        private String id;
        private String transmitter;
        private String message;
        
        @JsonProperty("sendDate")
        private String sendDate;
        
        private boolean owner;

        public LastInteraction() {
        }

        public LastInteraction(String id, String transmitter, String message, String sendDate, boolean owner) {
            this.id = id;
            this.transmitter = transmitter;
            this.message = message;
            this.sendDate = sendDate;
            this.owner = owner;
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
    }
} 