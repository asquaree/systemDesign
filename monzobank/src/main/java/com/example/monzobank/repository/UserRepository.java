package com.example.monzobank.repository;

import com.example.monzobank.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserRepository {

    private final HashMap<String, User> userMap = new HashMap<>();

    // Initialize test users
    {
        // User 1: aakash@gmail.com
        User user1 = new User();
        user1.setEmail("aakash@gmail.com");
        HashMap<String, Map<String, LocalDateTime>> urlHistory1 = new HashMap<>();
        
        Map<String, LocalDateTime> ianaVisited = new HashMap<>();
        ianaVisited.put("https://www.iana.org/protocols", LocalDateTime.of(2024, 6, 1, 10, 0));
        ianaVisited.put("https://www.iana.org/domains/root", LocalDateTime.of(2024, 6, 2, 11, 30));
        urlHistory1.put("https://www.iana.org/", ianaVisited);
        
        Map<String, LocalDateTime> googleVisited = new HashMap<>();
        googleVisited.put("https://www.google.com/search", LocalDateTime.of(2024, 6, 3, 14, 15));
        googleVisited.put("https://www.google.com/maps", LocalDateTime.of(2024, 6, 3, 14, 20));
        urlHistory1.put("https://www.google.com/", googleVisited);
        
        user1.setUrlLastAccessTime(urlHistory1);
        userMap.put(user1.getEmail(), user1);

        // User 2
        User user2 = new User();
        user2.setEmail("karan@example.com");
        
        // User 3
        User user3 = new User();
        user3.setEmail("aayush@test.com");
        HashMap<String, Map<String, LocalDateTime>> urlHistory3 = new HashMap<>();
        
        Map<String, LocalDateTime> githubVisited = new HashMap<>();
        githubVisited.put("https://github.com/explore", LocalDateTime.of(2024, 6, 7, 16, 45));
        urlHistory3.put("https://github.com/", githubVisited);
        
        user3.setUrlLastAccessTime(urlHistory3);
        userMap.put(user3.getEmail(), user3);
    }



    public User findByEmail(String email) {
        if (userMap != null) {
            User user = userMap.get(email);
            if (user != null) {
                log.debug("User found: {}", email);
            } else {
                log.debug("User not found: {}", email);
            }
            return user;
        }
        return null;
    }

    public Boolean markUrlsAsVisited(String parentUrl, String childUrl, String userEmail) {
        User user = userMap.get(userEmail);

        if (user == null) {
            log.warn("Cannot mark URL as visited - user not found: {}", userEmail);
            return false;
        }

        HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime = user.getUrlLastAccessTime();
        Map<String, LocalDateTime> childUrlAccessMap = urlLastAccessTime.getOrDefault(parentUrl, new HashMap<>());
        
        childUrlAccessMap.put(childUrl, LocalDateTime.now());
        urlLastAccessTime.put(parentUrl, childUrlAccessMap);
        user.setUrlLastAccessTime(urlLastAccessTime);
        userMap.put(userEmail, user);
        
        log.info("Marked URL as visited - Parent: {}, Child: {}, User: {}", parentUrl, childUrl, userEmail);
        return true;
    }

}
