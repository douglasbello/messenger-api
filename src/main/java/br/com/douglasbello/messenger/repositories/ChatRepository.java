package br.com.douglasbello.messenger.repositories;

import br.com.douglasbello.messenger.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}