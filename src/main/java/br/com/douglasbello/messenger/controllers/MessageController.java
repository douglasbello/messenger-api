package br.com.douglasbello.messenger.controllers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasbello.messenger.dto.MessageDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.MessageService;
import br.com.douglasbello.messenger.services.UserService;

@RestController
@RequestMapping("/api/v1/")
public class MessageController {
	private final MessageService messageService;
	private final UserService userService;
	private final ChatService chatService;

	private MessageController(MessageService messageService, UserService userService, ChatService chatService) {
		this.messageService = messageService;
		this.userService = userService;
		this.chatService = chatService;
	}

    @GetMapping(value = "/users/{userId}/chat/{chatId}/messages")
    private ResponseEntity<?> listAllMessages(@PathVariable Integer userId, @PathVariable Integer chatId) {
    	if (userService.isCurrentUser(userService.findById(userId).getUsername())) {
    		Chat chat = chatService.findById(chatId);
    		List<Message> messages = chat.getMessages();
    		List<MessageDTO> messagesDto = messages.stream().map(u -> new MessageDTO(u)).collect(Collectors.toList());
    		
    		return ResponseEntity.ok().body(messagesDto);
    	}
    	
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(HttpStatus.UNAUTHORIZED.value(), "User unauthorized!"));
    }

    @PostMapping(value = "/users/{userId}/chat/{chatId}/messages/send")
    private ResponseEntity<RequestResponseDTO> sendMessage(@PathVariable Integer userId, @PathVariable Integer chatId, @RequestBody String messageText) {
        if (chatService.findById(chatId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponseDTO(HttpStatus.NOT_FOUND.value(), "Chat doesn't exists!"));
        }
        if (userService.findById(userId) == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponseDTO(HttpStatus.NOT_FOUND.value(), "User doesn't exists."));
        }
        Chat chat = chatService.findById(chatId);

        if (!userService.isCurrentUser(userService.findById(userId).getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(HttpStatus.UNAUTHORIZED.value(), "User unauthorized!"));
        }

        User sender = userService.findById(userId);
        
        User receiver = new User();
        for (User participant : chat.getParticipants()) {
        	if (participant != sender) {
        		receiver = participant;
        	}
        }
        
        Message message = new Message(null, messageText, sender, receiver,chat);
        messageService.insert(message);
        return ResponseEntity.ok().body(new RequestResponseDTO(HttpStatus.OK.value(), "Message sent!"));
    }
}