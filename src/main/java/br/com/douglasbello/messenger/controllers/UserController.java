package br.com.douglasbello.messenger.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.douglasbello.messenger.entities.User;
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

    /* the return of this method is a ResponseEntity<> of type wildcard, so we can return either an object or a string */
    @GetMapping(value = "/{id}")
    private ResponseEntity<?> findUserById(@PathVariable Integer id) {
        try {
            User obj = userService.findById(id);
            UserDTO response = new UserDTO(obj);
            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exists!");
        }
    }

    @PostMapping(value = "/signIn")
    private ResponseEntity<String> signIn(@RequestBody User obj) {
        if (obj.getUsername().length() < 4 || obj.getUsername().length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST	).body("Username cannot be less than 4 characters or more than 20 characters.");
        }
        if (obj.getPassword().length() < 8 || obj.getPassword().length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be less than 8 characters or more than 100 characters.");
        }
        boolean result = userService.checkIfTheUsernameIsAlreadyUsed(obj.getUsername());
        if (result) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already in use!");
        }
        if (obj.getImgUrl() != null || obj.getToken() != null || obj.getId() != null || obj.getChats().size() > 0 ||
                obj.getFriends().size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must pass only username and password in the request!");
        }
        obj.setToken();
        userService.signIn(obj);
        return ResponseEntity.ok().body("Account created successfully!");
    }

    @PostMapping(value = "/login")
    private ResponseEntity<?> login(@RequestBody User user) {
        try {
            User db = userService.findUserByUsername(user.getUsername());

            if (db == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password incorrect.");
            }

            boolean result = userService.login(user, db);

            if (!result) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password incorrect.");
            }

            return ResponseEntity.ok().body(new UserDTO(db));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password incorrect.");
        }
    }

    @PostMapping(value = "/friendship-requests/send/{senderId}/{receiverId}")
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

    @PostMapping(value = "/friendship-requests/accept/{receiverId}/{requestId}")
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

    @PostMapping(value = "/friendship-requests/decline/{receiverId}/{requestId}")
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