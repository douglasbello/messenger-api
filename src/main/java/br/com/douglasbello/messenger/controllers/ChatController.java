package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;

    private final UserService userService;

    private ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
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
}