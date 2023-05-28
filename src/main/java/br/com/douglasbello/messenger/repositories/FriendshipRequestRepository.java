package br.com.douglasbello.messenger.repositories;

import br.com.douglasbello.messenger.entities.FriendshipRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Integer> {
}