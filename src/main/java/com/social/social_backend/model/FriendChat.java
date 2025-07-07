package com.social.social_backend.model;

public class FriendChat {
    private String id;
    private String name;
    private String avatar;
    private String lastMessage;
    private String lastMessageTime;
    private boolean online;

    public FriendChat() {
    }

    public FriendChat(String id, String name, String avatar, String lastMessage, String lastMessageTime, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.online = online;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}