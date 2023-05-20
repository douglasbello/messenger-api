package br.com.douglasbello.messenger.repositories;

import br.com.douglasbello.messenger.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}