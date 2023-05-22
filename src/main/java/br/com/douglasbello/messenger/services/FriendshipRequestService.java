package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.repositories.FriendshipRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipRequestService {
    private final FriendshipRequestRepository friendshipRequestRepository;

    public FriendshipRequestService(FriendshipRequestRepository friendshipRequestRepository) {
        this.friendshipRequestRepository = friendshipRequestRepository;
    }

    public List<FriendshipRequestDTO> findAll() {
        List<FriendshipRequest> result = friendshipRequestRepository.findAll();
        return result.stream().map(FriendshipRequestDTO::new).toList();
    }

    public FriendshipRequestDTO insert(FriendshipRequest obj) {
        FriendshipRequestDTO result = new FriendshipRequestDTO(friendshipRequestRepository.save(obj));
        return result;
    }
}