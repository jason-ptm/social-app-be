// MessageController.java
package com.social.social_backend.controller;

import com.social.social_backend.model.Message;
import com.social.social_backend.model.MessageRequest;
import com.social.social_backend.model.MessageResponse;
import com.social.social_backend.model.ErrorResponse;
import com.social.social_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = {"http://localhost", "http://localhost:5173"})
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatMessages(
            @PathVariable("id") String id,
            @RequestParam("current_user") String currentUser) {
        
        try {
            MessageResponse response = messageService.getChatMessages(id, currentUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> sendMessage(
            @PathVariable("id") String id,
            @RequestParam("from_user") String fromUser,
            @RequestParam("messageType") String messageType,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        try {
            // Basic validation
            if (fromUser == null || fromUser.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("El remitente es requerido", "error"));
            }

            MessageResponse response = messageService.sendMessage(id, fromUser, messageType, content, file);
            
            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }

    @GetMapping("/{chat_id}/message/{message_id}/file")
    public ResponseEntity<?> downloadFile(
            @PathVariable("chat_id") String chatId,
            @PathVariable("message_id") Integer messageId) {
        
        try {
            byte[] fileData = messageService.downloadFile(chatId, messageId);
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"file\"")
                .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), "error"));
        }
    }
}