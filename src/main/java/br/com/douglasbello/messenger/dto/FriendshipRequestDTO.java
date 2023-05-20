package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.entities.User;

import java.util.Objects;

public class FriendshipRequestDTO {
    private Integer id;
    private User sender;
    private User receiver;

    public FriendshipRequestDTO() {
    }

    public FriendshipRequestDTO(FriendshipRequest entity) {
        this.id = entity.getId();
        this.sender = entity.getSender();
        this.receiver = entity.getReceiver();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipRequestDTO that = (FriendshipRequestDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}