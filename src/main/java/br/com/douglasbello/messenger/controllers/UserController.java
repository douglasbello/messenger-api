package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.UserInputDTO;
import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import br.com.douglasbello.messenger.dto.TokenDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.security.TokenService;
import br.com.douglasbello.messenger.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    private UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    private ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok().body(new UserDTO(userService.getCurrentUser()));
    }

    @PostMapping(value = "/sign-in")
    private ResponseEntity<RequestResponseDTO> signIn(@RequestBody UserDTO obj) {
        if (obj.getUsername().length() < 4 || obj.getUsername().length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "Username cannot be less than 4 characters or more than 20 characters."));
        }
        if (obj.getPassword().length() < 8 || obj.getPassword().length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "Password cannot be less than 8 characters or more than 100 characters."));
        }
        if (userService.findUserByUsername(obj.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RequestResponseDTO(HttpStatus.CONFLICT.value(), "Username is already in use!"));
        }
        if (obj.getChats().size() != 0 || obj.getFriends().size() != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RequestResponseDTO(HttpStatus.BAD_REQUEST.value(), "You must not pass chats or friends ids in the sign in request."));
        }
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        user.setRole(user.getRole());
        userService.signIn(user);
        return ResponseEntity.ok().body(new RequestResponseDTO(HttpStatus.OK.value(), "Account created successfully!"));
    }

    @PostMapping(value = "/login")
    private ResponseEntity<?> login(@Valid @RequestBody UserInputDTO dto) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(new TokenDTO(token));
    }

    @GetMapping(value = "/friends")
    private ResponseEntity<List<UserDTO>> getAllUserFriends() {
        return ResponseEntity.ok().body(userService.getCurrentUser().getFriends().stream().map(UserDTO::new).collect(Collectors.toList()));
    }
}