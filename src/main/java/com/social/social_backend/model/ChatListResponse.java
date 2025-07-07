package com.social.social_backend.model;

import java.util.List;

public class ChatListResponse {
    private List<ChatItem> chats;
    private List<FriendChat> friends;
    private List<GroupChat> groups;

    public ChatListResponse(List<ChatItem> chats) {
        this.chats = chats;
    }

    public ChatListResponse() {
    }

    public List<ChatItem> getChats() { 
        return chats; 
    }
    
    public void setChats(List<ChatItem> chats) { 
        this.chats = chats; 
    }

    public List<FriendChat> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendChat> friends) {
        this.friends = friends;
    }

    public List<GroupChat> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupChat> groups) {
        this.groups = groups;
    }
}