package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.repositories.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
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

    private void updateData(Chat entity, Chat obj) {
        entity.setId(obj.getId());
        entity.getParticipants().addAll(obj.getParticipants());
        entity.getMessages().addAll(obj.getMessages());
    }

    public ChatDTO update(Integer id, Chat obj) {
        try {
            Chat entity = chatRepository.getReferenceById(id);
            updateData(entity,obj);
            return new ChatDTO(chatRepository.save(entity));
        } catch (EntityNotFoundException e) {
           throw new EntityNotFoundException();
        }
    }

    public void addMessageToChat(Message message) {
        messageService.insert(message);
        Chat chat = message.getChat();
        chat.getMessages().add(message);
        insert(chat);
    }
}