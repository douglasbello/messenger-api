package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.entities.User;

import java.util.*;

public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String imgUrl;
    private Set<User> friends = new HashSet<>();
    private Set<Chat> chats = new HashSet<>();
    private Set<FriendshipRequest> friendshipRequestsSent = new HashSet<>();
    private Set<FriendshipRequest> friendshipRequestsReceived = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.imgUrl = entity.getImgUrl();
        this.friends = entity.getFriends();
        this.chats = entity.getChats();
        this.friendshipRequestsSent = entity.getFriendshipRequestsSent();
        this.friendshipRequestsReceived = entity.getFriendshipRequestsReceived();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public Set<FriendshipRequest> getFriendshipRequestsSent() {
        return friendshipRequestsSent;
    }

    public Set<FriendshipRequest> getFriendshipRequestsReceived() {
        return friendshipRequestsReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", friends=" + friends +
                ", chats=" + chats +
                ", friendshipRequestsSent=" + friendshipRequestsSent +
                ", friendshipRequestsReceived=" + friendshipRequestsReceived +
                '}';
    }
}