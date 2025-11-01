package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;

import java.util.*;

public class BFSParsingStrategy implements ParsingStrategy {
    
    private static final int MAX_LINKS = 50;

    public BFSParsingStrategy() {
    }

    @Override
    public Url parse(String urlString, Parser parser, int maxDepth) {
        System.out.println("Parsing using BFS strategy for URL: " + urlString + " with depth: " + 
                          (maxDepth == Integer.MAX_VALUE ? "UNLIMITED" : maxDepth));
        
        Url rootUrl = new Url(urlString);
        Queue<UrlWithDepth> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Url> urlMap = new HashMap<>();
        
        queue.offer(new UrlWithDepth(urlString, 0));
        visited.add(urlString);
        urlMap.put(urlString, rootUrl);
        
        // BFS: Process all nodes at current level before going deeper
        while (!queue.isEmpty() && visited.size() < MAX_LINKS) {
            UrlWithDepth current = queue.poll();
            String currentUrl = current.url;
            int currentDepth = current.depth;
            
            // Stop if we've reached max depth
            if (currentDepth >= maxDepth) {
                continue;
            }
            
            // Use parser's extractLinks (HTML/JSON agnostic)
            List<String> extractedLinks = parser.extractLinks(currentUrl);
            List<Url> childUrls = new ArrayList<>();
            
            for (String link : extractedLinks) {
                if (UrlValidatorService.isValidLink(link) && !visited.contains(link) && visited.size() < MAX_LINKS) {
                    Url childUrl = new Url(link);
                    childUrls.add(childUrl);
                    visited.add(link);
                    urlMap.put(link, childUrl);
                    
                    // Add to queue for further crawling
                    queue.offer(new UrlWithDepth(link, currentDepth + 1));
                }
            }
            
            // Set children for current URL
            Url currentUrlObj = urlMap.get(currentUrl);
            if (currentUrlObj != null) {
                currentUrlObj.setChildUrls(childUrls);
            }
        }
        
        System.out.println("BFS completed. Visited " + visited.size() + " total URLs");
        return rootUrl;
    }
    
    // Helper class to track URL with its depth
    private static class UrlWithDepth {
        String url;
        int depth;
        
        UrlWithDepth(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }
}
