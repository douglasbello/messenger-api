package br.com.douglasbello.messenger.security;

import java.util.Collections;
import java.util.List;

import br.com.douglasbello.messenger.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class MyToken {
	private final UserService userService;
	
	
	protected MyToken(UserService userService) {
		this.userService = userService;
	}
	
	public Authentication decodeToken(HttpServletRequest request) {
		
		if (userService.count() != 0) {
			List<User> users = userService.getAll();
			for (User user : users) {
				System.out.println(user.getToken());
				if (request.getHeader("Authorization").equals("Bearer "+ user.getToken())) {
					return new UsernamePasswordAuthenticationToken(user.getUsername(), null, Collections.emptyList());
				}
			}
		}
		return null;
	}
}
