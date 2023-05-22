package br.com.douglasbello.messenger.config;

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

        userService.insertAll(Arrays.asList(user1,user2));

        FriendshipRequest friendshipRequest1 = new FriendshipRequest(null, user1,user2, FriendshipRequestStatus.WAITING_RESPONSE);
        friendshipRequestService.insert(friendshipRequest1);

//        Chat chat1 = new Chat(null);
//        chatService.insert(chat1);

        Chat chat1 = new Chat();
        chat1.getParticipants().add(user1);
        chat1.getParticipants().add(user2);
        chatService.insert(chat1);

        Message message = new Message(null,"bom dia!",user1,user2,chat1);
        messageService.insert(message);
    }
}