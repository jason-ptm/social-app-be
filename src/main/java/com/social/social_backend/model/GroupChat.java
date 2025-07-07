package com.social.social_backend.model;

public class GroupChat {
    private Integer id;
    private String name;
    private String avatar;
    private String lastMessage;
    private String lastMessageTime;
    private int memberCount;

    public GroupChat() {
    }

    public GroupChat(Integer id, String name, String avatar, String lastMessage, String lastMessageTime, int memberCount) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.memberCount = memberCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}