package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;

import java.util.*;

public class DFSParsingStrategy implements ParsingStrategy {
    
    private static final int MAX_LINKS = 50;

    public DFSParsingStrategy() {
    }

    @Override
    public Url parse(String urlString, Parser parser, int maxDepth) {
        System.out.println("Parsing using DFS strategy for URL: " + urlString + " with depth: " + 
                          (maxDepth == Integer.MAX_VALUE ? "UNLIMITED" : maxDepth));
        
        Set<String> visited = new HashSet<>();
        Url rootUrl = dfsRecursive(urlString, parser, visited, 0, maxDepth);
        
        System.out.println("DFS completed. Visited " + visited.size() + " total URLs");
        return rootUrl;
    }
    
    private Url dfsRecursive(String urlString, Parser parser, Set<String> visited, int currentDepth, int maxDepth) {
        Url currentUrl = new Url(urlString);
        visited.add(urlString);
        
        // Stop if we've reached max depth or visited too many URLs
        if (currentDepth >= maxDepth || visited.size() >= MAX_LINKS) {
            return currentUrl;
        }
        
        // Extract links from current page
        List<String> extractedLinks = parser.extractLinks(urlString);
        List<Url> childUrls = new ArrayList<>();
        
        for (String link : extractedLinks) {
            if (UrlValidatorService.isValidLink(link) && !visited.contains(link) && visited.size() < MAX_LINKS) {
                // DFS: Immediately recurse into child (go deep first)
                Url childUrl = dfsRecursive(link, parser, visited, currentDepth + 1, maxDepth);
                childUrls.add(childUrl);
            }
        }
        
        currentUrl.setChildUrls(childUrls);
        return currentUrl;
    }
}
