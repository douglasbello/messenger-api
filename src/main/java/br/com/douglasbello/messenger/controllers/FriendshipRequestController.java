package br.com.douglasbello.messenger.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.douglasbello.messenger.services.FriendshipRequestService;
import br.com.douglasbello.messenger.services.UserService;

@RestController
@RequestMapping(value = "/users/friendship-requests")
public class FriendshipRequestController {
	private final UserService userService;
	
	private final FriendshipRequestService friendshipRequestService;
	
	private FriendshipRequestController(UserService userService, FriendshipRequestService friendshipRequestService) {
		this.userService = userService;
		this.friendshipRequestService = friendshipRequestService;
	}
	
	@PostMapping(value = "/send/{senderId}/{receiverId}")
    private ResponseEntity<String> sendRequest(@RequestHeader("Authorization") String senderToken, @PathVariable Integer senderId, @PathVariable Integer receiverId) {

    	senderToken = senderToken.replace("Bearer ", "");
    	
    	if (!userService.checkIfTheTokenIsTheSameOfTheUser(senderToken, senderId)) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized for this request!");
    	}
    	
        if (friendshipRequestService.checkIfUserAlreadySentARequestToTheReceiver(senderId, receiverId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already sent an friend request to this user.");
        }

        if (userService.checkIfUsersAreAlreadyFriends(senderId, receiverId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: users are already friends");
        }

        boolean result = friendshipRequestService.sendRequest(senderId, receiverId);

        if (result) {
            return ResponseEntity.ok().body("Request created successful!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating friendship request!");
    }

    @PostMapping(value = "/accept/{receiverId}/{requestId}")
    private ResponseEntity<String> acceptRequest(@RequestHeader("Authorization") String receiverToken, @PathVariable Integer receiverId, @PathVariable Integer requestId) {
        
    	receiverToken = receiverToken.replace("Bearer ", "");
    	
    	if (!userService.checkIfTheTokenIsTheSameOfTheUser(receiverToken, receiverId)) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized");
    	}
    	
    	if (friendshipRequestService.acceptFriendRequest(receiverId, requestId)) {
            return ResponseEntity.ok("Friendship request accepted!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error!");
    }

    @PostMapping(value = "/decline/{receiverId}/{requestId}")
    private ResponseEntity<String> declineRequest(@RequestHeader("Authorization") String receiverToken, @PathVariable Integer receiverId, @PathVariable Integer requestId) {
        
    	receiverToken = receiverToken.replace("Bearer ", "");
    	
    	if (!userService.checkIfTheTokenIsTheSameOfTheUser(receiverToken, receiverId)) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User unauthorized!");
    	}
    	
    	if (friendshipRequestService.declineFriendRequest(receiverId, requestId)) {
            return ResponseEntity.ok("Friendship request declined!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error!");
    }
}