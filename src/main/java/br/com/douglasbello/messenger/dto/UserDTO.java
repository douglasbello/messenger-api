package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.User;

import java.util.*;

public class UserDTO {
    private Integer id;
    private String username;
    private String imgUrl;
    private Set<Integer> friends = new HashSet<>();
    private Set<Integer> chats = new HashSet<>();
    private String token;

    public UserDTO() {
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.imgUrl = entity.getImgUrl();
        this.friends = entity.getFriendsIds();
        this.chats = entity.getChatsIds();
        this.token = entity.getToken();
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public Set<Integer> getChats() {
        return chats;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                ", token=" + token +
                '}';
    }
}