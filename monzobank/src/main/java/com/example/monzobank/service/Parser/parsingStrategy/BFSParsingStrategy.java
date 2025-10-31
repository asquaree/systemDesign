package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;

import java.util.*;

public class BFSParsingStrategy implements ParsingStrategy {
    
    private static final int MAX_LINKS = 50;
    private final UrlValidatorService urlValidatorService;

    public BFSParsingStrategy(UrlValidatorService urlValidatorService) {
        this.urlValidatorService = urlValidatorService;
    }

    @Override
    public Url parse(String urlString, Parser parser) {
        System.out.println("Parsing using BFS strategy for URL: " + urlString);
        
        Url rootUrl = new Url(urlString);
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        List<Url> allChildUrls = new ArrayList<>();
        
        queue.offer(urlString);
        visited.add(urlString);
        
        // BFS: Process all nodes at current level before going deeper
        int processedCount = 0;
        while (!queue.isEmpty() && processedCount < 1) {
            String currentUrl = queue.poll();
            
            // Use parser's extractLinks (HTML/JSON agnostic)
            List<String> extractedLinks = parser.extractLinks(currentUrl);
            
            for (String link : extractedLinks) {
                if (urlValidatorService.isValidLink(link) && !visited.contains(link) && allChildUrls.size() < MAX_LINKS) {
                    allChildUrls.add(new Url(link));
                    visited.add(link);
                }
            }
            
            processedCount++;
        }
        
        rootUrl.setChildUrls(allChildUrls);
        System.out.println("BFS completed. Found " + allChildUrls.size() + " child URLs");
        return rootUrl;
    }
}
