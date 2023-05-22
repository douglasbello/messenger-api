package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    private final MessageService messageService;

    public ChatService(ChatRepository chatRepository, MessageService messageService) {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
    }

    public List<ChatDTO> findAll() {
        List<Chat> result = chatRepository.findAll();
        return result.stream().map(ChatDTO::new).toList();
    }

    public ChatDTO insert(Chat obj) {
        ChatDTO chatDTO = new ChatDTO(chatRepository.save(obj));
        return chatDTO;
    }

    public void addMessageToChat(Chat chat, Message message) {
        messageService.insert(message);
        chat.getMessages().add(message);
    }
}