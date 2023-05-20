package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;

import java.util.List;
import java.util.Objects;

public class ChatDTO {
    public Integer id;
    public List<Message> messages;
    public List<User> participants;

    public ChatDTO() {
    }

    public ChatDTO(Chat entity) {
        this.id = entity.getId();
        this.messages = entity.getMessages();
        this.participants = entity.getParticipants();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDTO chatDTO = (ChatDTO) o;
        return Objects.equals(id, chatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
                "id=" + id +
                ", messages=" + messages +
                ", participants=" + participants +
                '}';
    }
}