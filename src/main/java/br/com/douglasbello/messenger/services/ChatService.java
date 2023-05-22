package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<ChatDTO> findAll() {
        List<Chat> result = chatRepository.findAll();
        return result.stream().map(ChatDTO::new).toList();
    }

    public ChatDTO insert(Chat obj) {
        ChatDTO chatDTO = new ChatDTO(chatRepository.save(obj));
        return chatDTO;
    }
}