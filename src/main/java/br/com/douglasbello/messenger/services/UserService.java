package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.UserInputDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
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

    @Transactional
    public User update(Integer id, User obj) {
        try {
            User entity = userRepository.getReferenceById(id);
            updateData(entity, obj);
            return userRepository.save(entity);
        } catch ( EntityNotFoundException e ) {
            throw new RuntimeException();
        }
    }

    private void updateData(User entity, User obj) {
        entity.setUsername(obj.getUsername());
        entity.setPassword(obj.getPassword());
    }

    public Set<User> getAllFriendsByUserId(Integer userId) {
        return userRepository.findFriendsById(userId);
    }

    public long count() {
        return userRepository.count();
    }

    public boolean checkIfUsersAreAlreadyFriends(Integer senderId, Integer receiverId) {
        User sender = findById(senderId);
        User receiver = findById(receiverId);

        if ( sender.getFriends().contains(receiver) ) {
            return true;
        }
        return false;
    }

    public User signIn(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean login(UserInputDTO dto) {
        if ( userRepository.findUserByUsername(dto.username()) == null ) {
            return false;
        }
        return passwordEncoder.matches(dto.password(), userRepository.findUserByUsername(dto.username()).getPassword());
    }

    public User findUserByUsername(String username) {
        try {
            User obj = userRepository.findUserByUsername(username);
            return obj;
        } catch ( NoSuchElementException e ) {
            return null;
        }
    }

    public User getCurrentUser() {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserByUsername(auth.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}