package br.com.douglasbello.messenger.config;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.services.ChatService;
import br.com.douglasbello.messenger.services.FriendshipRequestService;
import br.com.douglasbello.messenger.services.MessageService;
import br.com.douglasbello.messenger.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile( "test" )
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

        User user1 = new User("user01", "user01");
        User user2 = new User("user02", "user02");
        User user3 = new User("douglas", "douglas");
        User user4 = new User("maria", "maria");

        userService.signIn(user1);
        userService.signIn(user2);
        userService.signIn(user3);
        userService.signIn(user4);

        friendshipRequestService.sendRequest(1, 2);
        friendshipRequestService.acceptFriendRequest(2, 1);
    }
}