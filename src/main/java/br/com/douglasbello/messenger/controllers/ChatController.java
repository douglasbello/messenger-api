package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;

    private ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    private ResponseEntity<List<ChatDTO>> findAll() {
        List<ChatDTO> result = chatService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    private ResponseEntity<ChatDTO> insert(@RequestBody Chat obj) {
        ChatDTO result = chatService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }
}