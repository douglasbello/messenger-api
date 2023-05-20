package br.com.douglasbello.messenger.repositories;

import br.com.douglasbello.messenger.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}