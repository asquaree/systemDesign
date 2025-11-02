package com.example.monzobank.entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    private String email;
    private HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime;

    public User(String email) {
        this.email = email;
        this.urlLastAccessTime = new HashMap<>();
    }
}
