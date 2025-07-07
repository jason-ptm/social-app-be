package com.social.social_backend.controller;

import com.social.social_backend.model.ChatListResponse;
import com.social.social_backend.model.SocialUser;
import com.social.social_backend.model.ErrorResponse;
import com.social.social_backend.service.SocialUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost", "http://localhost:5173"})
public class SocialUserController {

    private final SocialUserService service;

    public SocialUserController(SocialUserService service) {
        this.service = service;
    }

    @GetMapping("/login/{iduser}")
    public ResponseEntity<?> login(@PathVariable("iduser") String iduser) {
        try {
            SocialUser user = service.getUserById(iduser);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuario no encontrado", "error"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        
        try {
            // Basic validations
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("El nombre es obligatorio", "error"));
            }
            
            if (!isValidEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Formato de correo inválido", "error"));
            }
            
            if (phoneNumber == null || phoneNumber.length() < 10) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Número de teléfono muy corto", "error"));
            }

            SocialUser user = service.registerUser(name, email, phoneNumber, avatar);
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }

    @GetMapping("/user/{id}/chats")
    public ResponseEntity<?> getUserChats(@PathVariable("id") String userId) {
        try {
            ChatListResponse chats = service.getUserChats(userId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("[^@]+@[^@]+\\.[^@]+");
    }
}
