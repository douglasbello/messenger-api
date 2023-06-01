package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.MessageDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.MessageService;
import br.com.douglasbello.messenger.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;

    private final UserService userService;

    private final MessageService messageService;

    private ChatController(ChatService chatService, UserService userService, MessageService messageService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/create/{firstUserId}/{secondUserId}")
    private ResponseEntity<String> createChat(@RequestHeader("Authorization") String userToken, @PathVariable Integer firstUserId, @PathVariable Integer secondUserId) {
    	
    	if (!userService.checkIfUsersAreAlreadyFriends(firstUserId, secondUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Users aren't friends.");
        }

        if (chatService.checkIfAChatBetweenUsersAlreadyExists(firstUserId, secondUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A chat between these users already exists.");
        }

        chatService.createChat(firstUserId, secondUserId);
        return ResponseEntity.ok().body("Chat created!");
    }
    
    
    @GetMapping(value = "/messages/{chatId}")
    private ResponseEntity<?> listAll(@RequestHeader("Authorization") String participantToken, @PathVariable Integer chatId) {
    	if (chatService.getUserInChatByToken(participantToken, chatId) != null) {
    		Chat chat = chatService.findById(chatId);
    		List<Message> messages = chat.getMessages();
    		List<MessageDTO> messagesDto = messages.stream().map(u -> new MessageDTO(u)).collect(Collectors.toList());
    		
    		return ResponseEntity.ok().body(messagesDto);
    	}
    	
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized!");
    }

    @PostMapping(value = "/messages/send/{chatId}/{receiverId}")
    private ResponseEntity<String> sendMessage(@RequestHeader("Authorization") String senderToken, @PathVariable Integer chatId, Integer receiverId, @RequestBody String messageText) {
        try {
            Chat chat = chatService.findById(chatId);
            if (chat == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat doesn't exist!");
            }

            User sender = userService.findUserByToken(senderToken);
            if (sender == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized!");
            }

            User receiver = userService.findById(chatId);
            
            boolean result = chatService.checkIfTheChatContainsBothUsers(chatId, sender.getId(), receiverId);
            
            if (!result) {
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The chat doesn't contains the users!");
            }
            
            Message message = new Message(null, messageText, sender, receiver,chat);
            messageService.insert(message);
            return ResponseEntity.ok().body("Message sent!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users or chat not found.");
        }
    }
}