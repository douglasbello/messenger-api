package br.com.douglasbello.messenger.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table( name = "tb_messages" )
public class Message {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer id;
    private String messageText;

    @ManyToOne
    @JoinColumn( name = "sender_id" )
    private User sender;

    @ManyToOne
    @JoinColumn( name = "receiver_id" )
    private User receiver;

    private LocalDateTime sentAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn( name = "chat_id" )
    private Chat chat;

    public Message() {
    }

    public Message(Integer id, String messageText, User sender, User receiver, Chat chat) {
        this.id = id;
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;
        this.chat = chat;
        this.sentAt = LocalDateTime.now();
    }

    public Message(String messageText, User sender, User receiver) {
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = LocalDateTime.now();
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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", sentAt=" + sentAt +
                '}';
    }
}
