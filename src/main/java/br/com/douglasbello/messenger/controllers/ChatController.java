package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;

    private final UserService userService;

    private ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
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
}