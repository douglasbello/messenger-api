package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import br.com.douglasbello.messenger.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users/friendship-requests")
public class FriendshipRequestController {
    private final UserService userService;
    private final FriendshipRequestService friendshipRequestService;

    private FriendshipRequestController(UserService userService, FriendshipRequestService friendshipRequestService) {
        this.userService = userService;
        this.friendshipRequestService = friendshipRequestService;
    }

    @PostMapping(value = "/receiver/{receiverId}/send")
    private ResponseEntity<RequestResponseDTO> sendRequest(@PathVariable Integer receiverId) {
        User sender = userService.getCurrentUser();
        if (friendshipRequestService.checkIfUserAlreadySentARequestToTheReceiver(sender.getId(), receiverId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RequestResponseDTO(HttpStatus.CONFLICT.value(), "You already sent an friend request to this user."));
        }

        if (userService.checkIfUsersAreAlreadyFriends(sender.getId(), receiverId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new RequestResponseDTO(HttpStatus.FORBIDDEN.value(), "Error: users are already friends"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new RequestResponseDTO(HttpStatus.CREATED.value(), "Request created successfully!"));
    }

    @PostMapping(value = "/accept/{requestId}")
    private ResponseEntity<RequestResponseDTO> acceptRequest(@PathVariable Integer requestId) {
        User receiver = userService.getCurrentUser();

        friendshipRequestService.acceptFriendRequest(receiver.getId(), requestId);

        return ResponseEntity.ok(new RequestResponseDTO(HttpStatus.OK.value(), "Friendship request accepted!"));
    }

    @PostMapping(value = "/decline/{requestId}")
    private ResponseEntity<RequestResponseDTO> declineRequest(@PathVariable Integer requestId) {
        User receiver = userService.getCurrentUser();

        friendshipRequestService.declineFriendRequest(receiver.getId(), requestId);

        return ResponseEntity.ok(new RequestResponseDTO(HttpStatus.OK.value(), "Friendship request declined!"));
    }
}