package br.com.douglasbello.messenger.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.douglasbello.messenger.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
