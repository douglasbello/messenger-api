package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.MessageService;
import br.com.douglasbello.messenger.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @GetMapping
    private ResponseEntity<List<ChatDTO>> findAll() {
        List<ChatDTO> result = chatService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/create/{firstUserId}/{secondUserId}")
    private ResponseEntity<String> createChat(@PathVariable Integer firstUserId, @PathVariable Integer secondUserId) {
        if (!userService.checkIfUsersAreAlreadyFriends(firstUserId, secondUserId)) {
            return ResponseEntity.status(403).body("Users aren't friends.");
        }

        if (chatService.checkIfAChatBetweenUsersAlreadyExists(firstUserId, secondUserId)) {
            return ResponseEntity.status(403).body("A chat between these users already exists.");
        }

        chatService.createChat(firstUserId, secondUserId);
        return ResponseEntity.ok().body("Chat created!");
    }

    @PostMapping(value = "/message/{chatId}/{senderId}/{receiverId}")
    private ResponseEntity<String> sendMessage(@PathVariable Integer chatId, @PathVariable Integer senderId, @PathVariable Integer receiverId) {
        try {
            Chat chat = chatService.findById(chatId);
            if (chat == null) {
                return ResponseEntity.status(403).body("Chat doesn't exist!");
            }

            User sender = userService.findById(senderId);
            if (sender == null) {
                return ResponseEntity.status(403).body("User doesn't exist!");
            }

            if (!chatService.checkIfTheChatContainsTheUser(chatId, senderId,receiverId)) {
                return ResponseEntity.status(403).body("The chat doesn't contains the users given!");
            }

            User receiver = userService.findById(receiverId);

            Message message = new Message(null, "Hello World!", sender, receiver,chat);
            messageService.insert(message);
            return ResponseEntity.ok().body("Message sent!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(403).body("Internal error!");
        }
    }
}