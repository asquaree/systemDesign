package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;

import java.util.*;

public class DFSParsingStrategy implements ParsingStrategy {
    
    private static final int MAX_LINKS = 50;
    private final UrlValidatorService urlValidatorService;

    public DFSParsingStrategy(UrlValidatorService urlValidatorService) {
        this.urlValidatorService = urlValidatorService;
    }

    @Override
    public Url parse(String urlString, Parser parser) {
        System.out.println("Parsing using DFS strategy for URL: " + urlString);
        
        Url rootUrl = new Url(urlString);
        Set<String> visited = new HashSet<>();
        visited.add(urlString);
        
        // DFS: Extract links from current page (1 level only)
        List<String> extractedLinks = parser.extractLinks(urlString);
        List<Url> childUrls = new ArrayList<>();
        
        for (String link : extractedLinks) {
            if (urlValidatorService.isValidLink(link) && !visited.contains(link) && childUrls.size() < MAX_LINKS) {
                childUrls.add(new Url(link));
                visited.add(link);
            }
        }
        
        rootUrl.setChildUrls(childUrls);
        System.out.println("DFS completed. Found " + childUrls.size() + " child URLs");
        return rootUrl;
    }
}
