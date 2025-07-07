package com.social.social_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class SocialUser {
    private String id;
    private String name;
    private String email;
    private String avatar;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    private List<Object> chats;
    
    // Additional fields for internal use
    private String userName;
    private String userLastName;
    private String userUniqueName;
    private String locationCode;
    private LocalDateTime registrationDate;

    // Constructor for login response
    public SocialUser(String id, String name, String email, String avatar, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.chats = List.of(); // Empty list for chats
    }

    // Default constructor
    public SocialUser() {
        this.chats = List.of();
    }

    // Getters and setters for response fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<Object> getChats() { return chats; }
    public void setChats(List<Object> chats) { this.chats = chats; }

    // Getters and setters for internal fields
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getUserUniqueName() { return userUniqueName; }
    public void setUserUniqueName(String userUniqueName) { this.userUniqueName = userUniqueName; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    // Helper methods for compatibility
    public String getUserId() { return id; }
    public void setUserId(String userId) { this.id = userId; }
    
    public String getPhone() { return phoneNumber; }
    public void setPhone(String phone) { this.phoneNumber = phone; }

    // Helper method to build response object
    public static SocialUser buildResponse(String id, String userName, String userLastName, 
                                         String email, String avatar, String phone) {
        String fullName = userName + " " + userLastName;
        return new SocialUser(id, fullName, email, avatar, phone);
    }
}
