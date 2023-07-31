package br.com.douglasbello.messenger.dto;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.entities.enums.UserRole;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private UserRole role;
    private Set<Integer> friends = new HashSet<>();
    private Set<Integer> chats = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.role = entity.getRole();
        this.friends = entity.getFriendsIds();
        this.chats = entity.getChatsIds();
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

    public Set<Integer> getFriends() {
        return friends;
    }

    public Set<Integer> getChats() {
        return chats;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}