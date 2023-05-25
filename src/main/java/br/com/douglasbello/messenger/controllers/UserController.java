package br.com.douglasbello.messenger.controllers;

import java.net.URI;
import java.util.List;
import java.util.Set;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.entities.enums.FriendshipRequestStatus;
import br.com.douglasbello.messenger.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    private final FriendshipRequestService friendshipRequestService;

    private UserController(UserService userService, FriendshipRequestService friendshipRequestService) {
        this.userService = userService;
        this.friendshipRequestService = friendshipRequestService;
    }

    @GetMapping
    private ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<UserDTO> findUserById(@PathVariable Integer id) {
        User obj = userService.findById(id);
        UserDTO response = new UserDTO(obj);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/insert")
    private ResponseEntity<UserDTO> insert(@RequestBody User obj) {
        UserDTO result = userService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping(value = "/friendship-requests")
    private ResponseEntity<Set<FriendshipRequestDTO>> getAllFriendshipRequests() {
        Set<FriendshipRequestDTO> result = friendshipRequestService.findAll();
        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping(value = "/friendship-requests/send/{senderId}/{receiverId}")
    private ResponseEntity<String> sendRequest(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
    	boolean result = friendshipRequestService.sendRequest(senderId, receiverId);
    	
    	if (result) {            
        	return ResponseEntity.ok().body("Request created successful!");
    	}
    	return ResponseEntity.status(403).body("Error creating friendship request!");
    }

    @PostMapping(value = "/friendship-requests/accept/{receiverId}/{requestId}")
    private ResponseEntity<String> acceptRequest(@PathVariable Integer receiverId,@PathVariable Integer requestId) {
        if (friendshipRequestService.acceptFriendRequest(receiverId, requestId)) {
            return ResponseEntity.ok("Friendship request accepted!");
        }
        return ResponseEntity.status(403).body("Unexpected error!");
    }

    @PostMapping(value = "/friendship-requests/decline/{receiverId}/{requestId}")
    private ResponseEntity<String> declineRequest(@PathVariable Integer receiverId, @PathVariable Integer requestId) {
        if (friendshipRequestService.declineFriendRequest(receiverId, requestId)) {
            return ResponseEntity.ok("Friendship request declined!");
        }
        return ResponseEntity.status(403).body("Unexpected error!");
    }
}