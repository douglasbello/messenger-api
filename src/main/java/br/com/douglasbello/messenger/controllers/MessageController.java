package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.MessageDTO;
import br.com.douglasbello.messenger.dto.RequestResponseDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/api/v1" )
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;

    private MessageController(MessageService messageService, UserService userService, ChatService chatService) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatService = chatService;
    }

    @GetMapping( value = "/chat/{chatId}/messages" )
    private ResponseEntity<?> listAllMessages(@PathVariable Integer chatId) {
        Chat chat = chatService.findById(chatId);
        List<Message> messages = chat.getMessages();
        List<MessageDTO> messagesDto = messages.stream().map(u -> new MessageDTO(u)).collect(Collectors.toList());

        return ResponseEntity.ok().body(messagesDto);
    }

    @PostMapping( value = "/chat/{chatId}/messages/send" )
    private ResponseEntity<RequestResponseDTO> sendMessage(@PathVariable Integer chatId, @RequestBody String messageText) {
        if ( chatService.findById(chatId) == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestResponseDTO(HttpStatus.NOT_FOUND.value(), "Chat doesn't exists!"));
        }
        Chat chat = chatService.findById(chatId);

        User sender = userService.getCurrentUser();

        User receiver = new User();
        for ( User participant : chat.getParticipants() ) {
            if ( participant != sender ) {
                receiver = participant;
            }
        }

        Message message = new Message(null, messageText, sender, receiver, chat);
        messageService.insert(message);
        return ResponseEntity.ok().body(new RequestResponseDTO(HttpStatus.OK.value(), "Message sent!"));
    }
}