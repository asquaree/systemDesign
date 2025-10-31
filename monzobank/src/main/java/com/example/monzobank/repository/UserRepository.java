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
        Map<String, LocalDateTime> urlAccesstimeMap = new HashMap<>();
        user1 = new User();

        urlAccesstimeMap.put("www.google.com/home", LocalDateTime.now());
        urlLastAccessTime.put("www.google.com", urlAccesstimeMap);
        user1.setEmail("aakash@gmail.com");
        user1.setUrlLastAccessTime(urlLastAccessTime);
    }



    public User findByEmail(String email) {
        if (user1.getEmail().equals(email)) {
            return user1;
        }
        return null;
    }

}
