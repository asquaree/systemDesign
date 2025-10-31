package com.example.monzobank.service.Parser;

import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;

import java.util.ArrayList;
import java.util.List;

public class JsonParser extends Parser{
    public JsonParser(ParsingStrategy parsingStrategy) {
        super(parsingStrategy);
        this.parseType = "json";
        System.out.println("JsonParser created with strategy: " + parsingStrategy.getClass().getSimpleName());
    }

    @Override
    public List<String> extractLinks(String urlString) {
        List<String> links = new ArrayList<>();
        
        // TODO: Implement JSON link extraction
        // Expected format: JSON API that returns URLs
        // Example: {"urls": ["http://example.com/1", "http://example.com/2"]}
        
        System.out.println("JSON parsing not yet implemented for: " + urlString);
        
        return links;
    }
}
