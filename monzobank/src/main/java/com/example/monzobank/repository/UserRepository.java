package com.example.monzobank.repository;

import com.example.monzobank.entities.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    User user1;



    {
        HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime = new HashMap<>();
        Map<String, LocalDateTime> urlAccessTimeMap = new HashMap<>();
        user1 = new User();

        urlAccessTimeMap.put("www.google.com/home", LocalDateTime.now());
        urlLastAccessTime.put("www.google.com", urlAccessTimeMap);
        user1.setEmail("aakash@gmail.com");
        user1.setUrlLastAccessTime(urlLastAccessTime);

        HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime2 = new HashMap<>();

        Map<String, LocalDateTime> urlAccessTimeMap2 = new HashMap<>();

        user1 = new User();

        urlAccessTimeMap2.put("https://www.iana.org/protocols", LocalDateTime.of(2024, 6, 1, 10, 0));
        urlAccessTimeMap2.put("https://www.iana.org/domains/root", LocalDateTime.of(2024, 6, 2, 11, 30));
        urlLastAccessTime2.put("https://www.iana.org/", urlAccessTimeMap2);
        user1.setEmail("aakash@gmail.com");
        user1.setUrlLastAccessTime(urlLastAccessTime2);
    }



    public User findByEmail(String email) {
        if (user1.getEmail().equals(email)) {
            return user1;
        }
        return null;
    }

}
