package com.social.social_backend.service;

import com.social.social_backend.model.ChatListResponse;
import com.social.social_backend.model.FriendChat;
import com.social.social_backend.model.GroupChat;
import com.social.social_backend.model.SocialUser;
import com.social.social_backend.repository.SocialUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SocialUserService {

    private final SocialUserRepository repository;

    public SocialUserService(SocialUserRepository repository) {
        this.repository = repository;
    }
    
    public SocialUser getUserById(String id) {
        return repository.findById(id);
    }
    
    public List<SocialUser> getAllUsers() {
        return repository.findAll();
    }
    
    public boolean registerUser(SocialUser user) {
        return repository.register(user) > 0;
    }
    
    public SocialUser registerUser(String name, String email, String phoneNumber, MultipartFile avatar) {
        // Crear un nuevo usuario
        SocialUser user = new SocialUser();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        
        // Procesar avatar si existe
        if (avatar != null && !avatar.isEmpty()) {
            // Aquí deberías guardar el archivo y obtener la ruta
            user.setAvatar("avatar_" + System.currentTimeMillis() + ".jpg");
        } else {
            user.setAvatar("default_avatar.jpg");
        }
        
        // Generar ID único
        String userId = "user_" + System.currentTimeMillis();
        user.setId(userId);
        
        // Registrar en la base de datos
        if (repository.register(user) > 0) {
            return user;
        } else {
            throw new RuntimeException("Error al registrar usuario");
        }
    }
    
    public ChatListResponse getUserChats(String userId) {
        List<FriendChat> friends = repository.findFriends(userId);
        List<GroupChat> groups = repository.findGroups(userId);
        ChatListResponse response = new ChatListResponse();
        response.setFriends(friends);
        response.setGroups(groups);
        return response;
    }
}
