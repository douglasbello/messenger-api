package br.com.douglasbello.messenger.controllers;

import br.com.douglasbello.messenger.dto.UserInputDTO;
import br.com.douglasbello.messenger.dto.RequestResponseDTO;
import br.com.douglasbello.messenger.dto.TokenDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.entities.enums.UserRole;
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
    private ResponseEntity<?> signIn(@Valid @RequestBody UserInputDTO obj) {

        if (userService.findUserByUsername(obj.username()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RequestResponseDTO(HttpStatus.CONFLICT.value(), "Username is already in use!"));
        }

        User user = new User(obj.username(), obj.password(), UserRole.USER);
        UserDTO response = new UserDTO(userService.signIn(user));
        return ResponseEntity.ok().body(response);
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