package br.com.douglasbello.messenger.config;

import br.com.douglasbello.messenger.dto.FriendshipRequestDTO;
import br.com.douglasbello.messenger.dto.UserDTO;
import br.com.douglasbello.messenger.entities.Chat;
import br.com.douglasbello.messenger.entities.FriendshipRequest;
import br.com.douglasbello.messenger.entities.Message;
import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.entities.enums.FriendshipRequestStatus;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import br.com.douglasbello.messenger.services.MessageService;
import br.com.douglasbello.messenger.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class InsertTest implements CommandLineRunner {

    private final UserService userService;
    private final FriendshipRequestService friendshipRequestService;
    private final MessageService messageService;
    private final ChatService chatService;

    public InsertTest(UserService userService, FriendshipRequestService friendshipRequestService,
                       MessageService messageService, ChatService chatService) {
        this.userService = userService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @Override
    public void run(String... args) throws Exception {

        User user1 = new User(null, "user01", "user01");
        User user2 = new User(null, "user02", "user02");
        User user3 = new User(null, "douglas", "douglas");
        User user4 = new User(null, "maria", "maria");

        userService.insertAll(Arrays.asList(user1, user2, user3, user4));

        friendshipRequestService.sendRequest(1, 2);

        Chat chat1 = new Chat();
        chat1.getParticipants().add(user1);
        chat1.getParticipants().add(user2);
        chatService.insert(chat1);

        Message message1 = new Message(null,"bom dia!",user1,user2,chat1);
        chatService.addMessageToChat(message1);

        Message message2 = new Message(null,"good night",user2,user1,chat1);
        chatService.addMessageToChat(message2);

//        result.setStatus(FriendshipRequestStatus.ACCEPTED);
//        friendshipRequestService.acceptFriendRequest(friendshipRequest1.getReceiver().getId(), friendshipRequest1.getId());
//        System.out.println(userService.getAllFriendsByUserId(1));
    }
}