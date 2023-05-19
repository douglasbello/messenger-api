package br.com.douglasbello.messenger.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User insert(User user) {
        return userRepository.save(user);
    }
}