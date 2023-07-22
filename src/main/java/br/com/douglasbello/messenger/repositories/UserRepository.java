package br.com.douglasbello.messenger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.douglasbello.messenger.entities.User;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.friends FROM User u WHERE u.id = :userId")
    Set<User> findFriendsById(@Param("userId") Integer userId);

    User findUserByUsername(String username);
    
    User findUserByToken(String token);
    
    UserDetails findByUsername(String username);
}