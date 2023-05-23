package br.com.douglasbello.messenger.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String imgUrl;

    @ManyToMany
    @JoinTable(
            name = "tb_user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    public void addFriend(User friend) {
        friends.add(friend);
        friend.getFriends().add(this);
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "participants")
    private Set<Chat> chats = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messagesSent = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> messagesReceived = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<FriendshipRequest> friendshipRequestsSent = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private Set<FriendshipRequest> friendshipRequestsReceived = new HashSet<>();

    public User() {
    }
   
    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public Set<FriendshipRequest> getFriendshipRequestsSent() {
        return friendshipRequestsSent;
    }

    public Set<FriendshipRequest> getFriendshipRequestsReceived() {
        return friendshipRequestsReceived;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public Set<Integer> getChatsIds() {
        Set<Integer> ids = chats.stream().map(Chat::getId).collect(Collectors.toSet());
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id = " + id +
                " username = " + username +
                " imgUrl = " + imgUrl;
    }
}
