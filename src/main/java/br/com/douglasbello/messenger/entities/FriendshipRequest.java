package br.com.douglasbello.messenger.entities;

import br.com.douglasbello.messenger.entities.enums.FriendshipRequestStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_friendship_request")
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private Integer status;

    public FriendshipRequest() {
    }

    public FriendshipRequest(Integer id, User sender, User receiver, FriendshipRequestStatus status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        setStatus(status);
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

    public FriendshipRequestStatus getStatus() {
        return FriendshipRequestStatus.valueOf(status);
    }

    public void setStatus(FriendshipRequestStatus status) {
        if (status != null) {
            this.status = status.getCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipRequest that = (FriendshipRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}