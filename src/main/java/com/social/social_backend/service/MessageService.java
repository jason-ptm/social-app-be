// MessageService.java
package com.social.social_backend.service;

import com.social.social_backend.model.Message;
import com.social.social_backend.model.MessageRequest;
import com.social.social_backend.model.MessageResponse;
import com.social.social_backend.repository.MessageRepository;
import com.social.social_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public MessageResponse sendMessage(MessageRequest request) {
        // Validar usuarios
        if (!userRepository.userExists(request.getSenderUserId())) {
            return new MessageResponse(false, "El remitente no existe");
        }

        if (request.getReceiverUserId() != null &&
                !userRepository.userExists(request.getReceiverUserId())) {
            return new MessageResponse(false, "El receptor no existe");
        }

        // Enviar mensaje
        Integer messageId = messageRepository.sendMessageWithContent(request);

        if (messageId != null && messageId > 0) {
            return new MessageResponse(true, "Mensaje enviado");
        }
        return new MessageResponse(false, "Error al enviar mensaje");
    }

    public MessageResponse sendMessage(String chatId, String fromUser, String messageType, String content, MultipartFile file) {
        try {
            // Crear MessageRequest
            MessageRequest request = new MessageRequest();
            request.setSenderUserId(fromUser);
            
            // Determinar si es chat privado o grupo
            if (chatId.startsWith("group_")) {
                // Es un grupo
                String groupId = chatId.substring(6); // Remover "group_" prefix
                request.setGroupId(Integer.parseInt(groupId));
            } else {
                // Es un chat privado
                request.setReceiverUserId(chatId);
            }
            
            request.setMessageType(messageType);
            request.setContent(content);
            
            // Procesar archivo si existe
            if (file != null && !file.isEmpty()) {
                // Aquí deberías guardar el archivo y obtener la ruta
                request.setContent("Archivo: " + file.getOriginalFilename());
            }
            
            return sendMessage(request);
        } catch (Exception e) {
            return new MessageResponse(false, "Error al enviar mensaje: " + e.getMessage());
        }
    }

    public MessageResponse getChatMessages(String chatId, String currentUser) {
        try {
            List<MessageResponse.MessageItem> messages;
            String chatType;
            
            if (chatId.startsWith("group_")) {
                // Es un grupo
                String groupId = chatId.substring(6); // Remover "group_" prefix
                List<Message> groupMessages = getGroupMessages(Integer.parseInt(groupId));
                messages = convertToMessageItems(groupMessages, currentUser);
                chatType = "group";
            } else {
                // Es un chat privado
                List<Message> privateMessages = getPrivateMessages(currentUser, chatId);
                messages = convertToMessageItems(privateMessages, currentUser);
                chatType = "private";
            }
            
            return new MessageResponse(chatType, messages);
        } catch (Exception e) {
            return new MessageResponse(false, "Error al obtener mensajes: " + e.getMessage());
        }
    }

    private List<MessageResponse.MessageItem> convertToMessageItems(List<Message> messages, String currentUser) {
        return messages.stream()
                .map(msg -> new MessageResponse.MessageItem(
                        msg.getId().toString(),
                        msg.getSenderUserId(),
                        msg.getContent(),
                        msg.getSendDate().toString(),
                        msg.getSenderUserId().equals(currentUser),
                        "" // avatar placeholder
                ))
                .toList();
    }

    public byte[] downloadFile(String chatId, Integer messageId) {
        // Implementar descarga de archivo
        // Por ahora retornamos un array vacío
        return new byte[0];
    }

    public List<Message> getPrivateMessages(String userId1, String userId2) {
        // Validar usuarios
        if (!userRepository.userExists(userId1) || !userRepository.userExists(userId2)) {
            throw new IllegalArgumentException("Uno o ambos usuarios no existen");
        }
        return messageRepository.getMessages(userId1, userId2);
    }

    public List<Message> getGroupMessages(Integer groupId) {
        // Aquí deberías validar si el grupo existe
        return messageRepository.getGroupMessages(groupId);
    }
}