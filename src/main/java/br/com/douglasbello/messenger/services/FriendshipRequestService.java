package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public Set<FriendshipRequestDTO> findAll() {
        List<FriendshipRequest> result = friendshipRequestRepository.findAll();
        return result.stream().map(FriendshipRequestDTO::new).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public FriendshipRequest findById(Integer id) {
        Optional<FriendshipRequest> obj = friendshipRequestRepository.findById(id);
        return obj.get();
    }

    @Transactional
    public boolean sendRequest(Integer senderId, Integer receiverId) {
    	try {
    		User sender = userService.findById(senderId);
    		User receiver = userService.findById(receiverId);
    		
    		if (sender != null && receiver != null) {
    			FriendshipRequest entity = new FriendshipRequest(null, sender, receiver, FriendshipRequestStatus.WAITING_RESPONSE);
    			friendshipRequestRepository.save(entity);
    			
    			return true;
    		}
    		
    	} catch (NoSuchElementException e) {
    		return false;
    	}
        return false;
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

    public boolean update(Integer id, FriendshipRequest obj) {
        try {
            FriendshipRequest entity = friendshipRequestRepository.getReferenceById(id);
            updateData(entity, obj);
            return true;
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
    public boolean declineFriendRequest(Integer receiverId , Integer requestId) {
        try {
            FriendshipRequest friendshipRequest = findById(requestId);
            if (friendshipRequest != null && Objects.equals(receiverId, friendshipRequest.getReceiver().getId())) {
                delete(requestId);
                return true;
            }

        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    @Transactional
    public boolean acceptFriendRequest(Integer receiverId, Integer requestId) {
        try {
            FriendshipRequest friendshipRequest = findById(requestId);
            if (friendshipRequest != null && Objects.equals(receiverId, friendshipRequest.getReceiver().getId())) {
                User receiver = userService.findById(friendshipRequest.getReceiver().getId());
                User sender = userService.findById(friendshipRequest.getSender().getId());
                receiver.getFriends().add(sender);
                sender.getFriends().add(receiver);
                friendshipRequest.setStatus(FriendshipRequestStatus.ACCEPTED);
                update(requestId, friendshipRequest);
                userService.update(receiver.getId(), receiver);
                userService.update(sender.getId(), sender);
                return true;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }
}