package br.com.douglasbello.messenger.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.douglasbello.messenger.entities.User;

@Service
public class TokenService {
	@Value("${api.security.token.secret}")
	public String SECRET_KEY;
	public final String ISSUER = "douglasbello";
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("SECRET_KEY");
			String token = JWT.create()
					.withIssuer(ISSUER)
					.withExpiresAt(generateExpiritationDate())
					.sign(algorithm);
			
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.require(algorithm)
					.withIssuer(ISSUER)
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException exception) {
			return "";
		}
	}
	
	
	
	
	
	
	
	private Instant generateExpiritationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
