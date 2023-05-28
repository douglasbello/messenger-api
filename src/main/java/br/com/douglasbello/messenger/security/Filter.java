package br.com.douglasbello.messenger.security;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.douglasbello.messenger.dto.RequestErrorDTO;
import br.com.douglasbello.messenger.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class Filter extends OncePerRequestFilter {
	
	private final UserRepository userRepository;
	
	protected Filter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws JsonProcessingException, IOException, ServletException {
		if (request.getHeader("Authorization") != null) {
			Token token = new Token(userRepository);
			Authentication auth = token.decodeToken(request);
			
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			else {
				RequestErrorDTO error = new RequestErrorDTO(401,"User unauthorized!");
				response.setStatus(error.getStatus());
				response.setContentType("application/json");
				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().print(mapper.writeValueAsString(error));
				response.getWriter().flush();
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
