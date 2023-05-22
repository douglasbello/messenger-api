package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class MessageDTO {
    private Integer id;
    private String messageText;
    private User sender;
    private User receiver;
    private LocalDateTime sentAt;
    private Integer chat;

    public MessageDTO() {
    }

    public MessageDTO(Message entity) {
        this.id = entity.getId();
        this.messageText = entity.getMessageText();
        this.sender = entity.getSender();
        this.receiver = entity.getReceiver();
        this.sentAt = entity.getSentAt();
        this.chat = entity.getChat().getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Integer getChat() {
        return chat;
    }

    public void setChat(Integer chat) {
        this.chat = chat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", sentAt=" + sentAt +
                ", chat=" + chat +
                '}';
    }
}