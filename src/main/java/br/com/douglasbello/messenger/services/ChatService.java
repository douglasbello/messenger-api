package br.com.douglasbello.messenger.services;

import br.com.douglasbello.messenger.dto.ChatDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    private final MessageService messageService;
    
    private final UserService userService;

    public ChatService(ChatRepository chatRepository, MessageService messageService, UserService userService) {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ChatDTO> findAll() {
        List<Chat> result = chatRepository.findAll();
        return result.stream().map(ChatDTO::new).toList();
    }

    public Chat findById(Integer id) {
        Optional<Chat> obj = chatRepository.findById(id);
        return obj.get();
    }

    @Transactional
    public boolean createChat(Integer firstUserId, Integer secondUserId) {
        try {
            User firstUser = userService.findById(firstUserId);
            User secondUser = userService.findById(secondUserId);

            if (firstUser == null || secondUser == null) {
                return false;
            }

            Chat chat = new Chat();
            chat.getParticipants().add(firstUser);
            chat.getParticipants().add(secondUser);
            chatRepository.save(chat);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
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
        update(chat.getId(), chat);
    }
    
    @Transactional
    public boolean checkIfAChatBetweenUsersAlreadyExists(Integer firstUserId, Integer secondUserId) {
    	List<Chat> chats = chatRepository.findAll();
    	
    	for (Chat chat : chats) {
    		User firstUser = userService.findById(firstUserId);
    		User secondUser = userService.findById(secondUserId);
    		
    		if (chat.getParticipants().contains(firstUser) && chat.getParticipants().contains(secondUser)) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    @Transactional
    public boolean checkIfTheChatContainsTheUser(Integer chatId, Integer senderId, Integer receiverId) {
        try {
            Chat chat = findById(chatId);
            User sender = userService.findById(senderId);
            User receiver = userService.findById(receiverId);

            if (!chat.getParticipants().contains(sender) || !chat.getParticipants().contains(receiver)) {
                return false;
            }

            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}