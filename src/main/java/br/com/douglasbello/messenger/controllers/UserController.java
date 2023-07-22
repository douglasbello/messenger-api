package br.com.douglasbello.messenger.controllers;

import java.util.NoSuchElementException;

import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping(value = "/sign-in")
    private ResponseEntity<RequestResponseDTO> signIn(@RequestBody UserDTO obj) {
        if (obj.getUsername().length() < 4 || obj.getUsername().length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "Username cannot be less than 4 characters or more than 20 characters."));
        }
        if (obj.getPassword().length() < 8 || obj.getPassword().length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "Password cannot be less than 8 characters or more than 100 characters."));
        }
        boolean result = userService.checkIfTheUsernameIsAlreadyUsed(obj.getUsername());
        if (result) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RequestResponseDTO(HttpStatus.CONFLICT.value(), "Username is already in use!"));
        }
        if (obj.getChats().size() != 0 || obj.getFriends().size() != 0) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "You must not pass chats or friends ids in the sign in request."));
        }
        userService.signIn(obj);
        return ResponseEntity.ok().body(new RequestResponseDTO(HttpStatus.OK.value(), "Account created successfully!"));
    }

    @PostMapping(value = "/login")
    private ResponseEntity<?> login(@RequestBody User user) {
        try {
            User db = userService.findUserByUsername(user.getUsername());

            if (db == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Username or password incorrect."));
            }

            boolean result = userService.login(user, db);

            if (!result) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Username or password incorrect."));
            }

            return ResponseEntity.ok().body(new UserDTO(db));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RequestResponseDTO(HttpStatus.UNAUTHORIZED.value(), "Username or password incorrect."));
        }
    }
}