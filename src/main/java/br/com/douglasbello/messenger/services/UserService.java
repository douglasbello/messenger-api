package br.com.douglasbello.messenger.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import br.com.douglasbello.messenger.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        List<User> result = userRepository.findAll();
        return result.stream().map(UserDTO::new).toList();
    }

    public User findById(Integer id) {
        Optional<User> obj = userRepository.findById(id);
        return obj.get();
    }

    public UserDTO insert(User obj) {
        UserDTO result = new UserDTO(userRepository.save(obj));
        return result;
    }

    @Transactional
    public void insertAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Transactional
    public User update(Integer id, User obj) {
        try {
            User entity = userRepository.getReferenceById(id);
            updateData(entity,obj);
            return userRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private void updateData(User entity, User obj) {
        entity.setUsername(obj.getUsername());
        entity.setPassword(obj.getPassword());
        entity.setImgUrl(obj.getImgUrl());
    }

    public Set<User> getAllFriendsByUserId(Integer userId) {
        Set<User> friends = userRepository.findFriendsById(userId);
        return friends;
    }

    @Transactional
    public boolean checkIfUsersAreAlreadyFriends(Integer senderId, Integer receiverId) {
        User sender = findById(senderId);
        User receiver = findById(receiverId);

        if (sender.getFriends().contains(receiver)) {
            return true;
        }
        return false;
    }

    public void encoding(User obj) {
        obj.setPassword(passwordEncoder.encode(obj.getPassword()));
    }

    public boolean checkIfTheUsernameIsAlreadyUsed(String username) {
        List<User> users = userRepository.findAll();

        for (User obj : users) {
            if (obj.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void signIn(User user) {
        passwordEncoder.encode(user.getPassword());
        userRepository.save(user);
    }

    public boolean login(User user, User db) {
        return passwordEncoder.matches(user.getPassword(), db.getPassword());
    }

    public User findUserByUsername(String username) {
        try {
            User obj = userRepository.findUserByUsername(username);
            return obj;
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}