package br.com.douglasbello.messenger.services;

import java.util.List;

import br.com.douglasbello.messenger.dto.UserDTO;
import org.springframework.stereotype.Service;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        List<User> result = userRepository.findAll();
        return result.stream().map(UserDTO::new).toList();
    }

    public UserDTO insert(User obj) {
        UserDTO result = new UserDTO(userRepository.save(obj));
        return result;
    }

    public void insertAll(List<User> users) {
        userRepository.saveAll(users);
    }
}