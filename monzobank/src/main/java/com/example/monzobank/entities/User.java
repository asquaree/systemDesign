package com.example.monzobank.entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    private String email;
    private HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Map<String, LocalDateTime>> getUrlLastAccessTime() {
        return urlLastAccessTime;
    }

    public void setUrlLastAccessTime(HashMap<String, Map<String, LocalDateTime>> urlLastAccessTime) {
        this.urlLastAccessTime = urlLastAccessTime;
    }
}
