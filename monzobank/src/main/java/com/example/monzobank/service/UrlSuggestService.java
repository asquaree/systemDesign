package com.example.monzobank.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class UrlSuggestService {


    // Service methods for URL suggestions would go here

    public static ResponseEntity<List<String>> suggestUrls(String query) {
        // Implementation for suggesting URLs based on the query
        // trie can be used or elastic search
        return ResponseEntity.ok(List.of("http://example.com/suggested1", "http://example.com/suggested2"));
    }
}
