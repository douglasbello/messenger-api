package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.entities.enums.FriendshipRequestStatus;
import br.com.douglasbello.messenger.repositories.FriendshipRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipRequestService {
    private final FriendshipRequestRepository friendshipRequestRepository;

    private final UserService userService;

    @PersistenceContext
    private EntityManager entityManager;


    public FriendshipRequestService(FriendshipRequestRepository friendshipRequestRepository, UserService userService) {
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<FriendshipRequestDTO> findAll() {
        List<FriendshipRequest> result = friendshipRequestRepository.findAll();
        return result.stream().map(FriendshipRequestDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public FriendshipRequest findById(Integer id) {
        Optional<FriendshipRequest> obj = friendshipRequestRepository.findById(id);
        return obj.orElseThrow(RuntimeException::new);
    }

    @Transactional
    public FriendshipRequestDTO insert(FriendshipRequest obj) {
        FriendshipRequestDTO result = new FriendshipRequestDTO(friendshipRequestRepository.save(obj));
        return result;
    }

    @Transactional
    public void delete(Integer id) {
        try {
            friendshipRequestRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
           e.printStackTrace();
        } catch (DataIntegrityViolationException exception) {
            exception.printStackTrace();
        }
    }

    public FriendshipRequestDTO update(Integer id, FriendshipRequest obj) {
        try {
            FriendshipRequest entity = friendshipRequestRepository.getReferenceById(id);
            updateData(entity, obj);
            return insert(obj);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private void updateData(FriendshipRequest entity, FriendshipRequest obj) {
        entity.setStatus(entity.getStatus());
        entity.setSender(entity.getSender());
        entity.setReceiver(entity.getReceiver());
    }

    @Transactional
    public void declineFriendRequest(FriendshipRequest friendshipRequest) {
        delete(friendshipRequest.getId());
    }

    @Transactional
    public void acceptFriendRequest(Integer id) {
        FriendshipRequest friendshipRequest = findById(id);
        User receiver = userService.findById(friendshipRequest.getReceiver().getId());
        User sender = userService.findById(friendshipRequest.getSender().getId());
        receiver.addFriend(sender);
        friendshipRequest.setStatus(FriendshipRequestStatus.ACCEPTED);
        update(id, friendshipRequest);
        userService.update(receiver.getId(), receiver);
    }
}