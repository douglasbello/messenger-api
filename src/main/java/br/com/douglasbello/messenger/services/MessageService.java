package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.MessageDTO;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDTO> findAll() {
        List<Message> result = messageRepository.findAll();
        return result.stream().map(MessageDTO::new).toList();
    }

    public MessageDTO insert(Message obj) {
        MessageDTO result = new MessageDTO(messageRepository.save(obj));
        return result;
    }
}