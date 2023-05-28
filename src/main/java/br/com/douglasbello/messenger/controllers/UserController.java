package br.com.douglasbello.messenger.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.security.Token;
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

    @PostMapping(value = "/signIn")
    private ResponseEntity<String> signIn(@RequestBody User obj) {
        boolean result = userService.checkIfTheUsernameIsAlreadyUsed(obj.getUsername());
        if (result) {
            return ResponseEntity.status(403).body("Username is already in use!");
        }

        userService.signIn(obj);
        return ResponseEntity.ok().body("Account created successfully!");
    }

    @PostMapping(value = "/login")
    private ResponseEntity<String> login(@RequestBody User user) {
        try {
            User db = userService.findUserByUsername(user.getUsername());

            if (db == null) {
                return ResponseEntity.status(403).body("Username or password incorrects.");
            }

            boolean result = userService.login(user, db);

            if (!result) {
                return ResponseEntity.status(403).body("Username or password incorrects./2");
            }

            return ResponseEntity.ok("Bearer " + db.getToken());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(403).body("Username or password incorrects./3");
        }
    }

    @GetMapping(value = "/friendship-requests")
    private ResponseEntity<Set<FriendshipRequestDTO>> getAllFriendshipRequests() {
        Set<FriendshipRequestDTO> result = friendshipRequestService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/friendship-requests/send/{senderId}/{receiverId}")
    private ResponseEntity<String> sendRequest(@PathVariable Integer senderId, @PathVariable Integer receiverId) {

        if (friendshipRequestService.checkIfUserAlreadySentARequestToTheReceiver(senderId, receiverId)) {
            return ResponseEntity.status(403).body("You already sent an friend request to this user.");
        }

        if (userService.checkIfUsersAreAlreadyFriends(senderId, receiverId)) {
            return ResponseEntity.status(403).body("Error: users are already friends");
        }

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
