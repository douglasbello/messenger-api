package br.com.douglasbello.messenger.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@RequestMapping(value = "/chat/messages")
public class MessageController {
	private final MessageService messageService;
	private final UserService userService;
	private final ChatService chatService;
	
	private MessageController(MessageService messageService, UserService userService, ChatService chatService) {
		this.messageService = messageService;
		this.userService = userService;
		this.chatService = chatService;
	}

    @GetMapping(value = "/{chatId}")
    private ResponseEntity<?> listAll(@RequestHeader("Authorization") String participantToken, @PathVariable Integer chatId) {
    	if (chatService.getUserInChatByToken(participantToken, chatId) != null) {
    		Chat chat = chatService.findById(chatId);
    		List<Message> messages = chat.getMessages();
    		List<MessageDTO> messagesDto = messages.stream().map(u -> new MessageDTO(u)).collect(Collectors.toList());
    		
    		return ResponseEntity.ok().body(messagesDto);
    	}
    	
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(401,"User unauthorized!"));
    }

    @PostMapping(value = "/send/{chatId}/{receiverId}")
    private ResponseEntity<RequestResponseDTO> sendMessage(@RequestHeader("Authorization") String senderToken, @PathVariable Integer chatId, Integer receiverId, @RequestBody String messageText) {
        try {
            Chat chat = chatService.findById(chatId);
            if (chat == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponseDTO(404,"Chat doesn't exist!"));
            }

            User sender = userService.findUserByToken(senderToken);
            if (sender == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(401,"User unauthorized!"));
            }

            User receiver = userService.findById(chatId);
            
            boolean result = chatService.checkIfTheChatContainsBothUsers(chatId, sender.getId(), receiverId);
            
            if (!result) {
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(401,"The chat doesn't contains the users!"));
            }
            
            Message message = new Message(null, messageText, sender, receiver,chat);
            messageService.insert(message);
            return ResponseEntity.ok().body(new RequestResponseDTO(200,"Message sent!"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponseDTO(404,"Users or chat not found."));
        }
    }
}