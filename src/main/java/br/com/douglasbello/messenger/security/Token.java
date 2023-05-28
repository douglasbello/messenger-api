package br.com.douglasbello.messenger.security;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.douglasbello.messenger.entities.User;
import br.com.douglasbello.messenger.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class Token {
	private final UserRepository userRepository;
	
	
	protected Token(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Authentication decodeToken(HttpServletRequest request) {
		
		if (userRepository.count() != 0) {
			List<User> users = userRepository.findAll();
			for (User user : users) {
				if (request.getHeader("Authorization").equals("Bearer "+ user.getToken())) {
					return new UsernamePasswordAuthenticationToken(user.getUsername(), null);
				}
			}
		}
		return null;
	}
}
