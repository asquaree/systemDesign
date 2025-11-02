package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.ParseFactory;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.Parser.parsingStrategy.BFSParsingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UrlSearchService {



    @Autowired
    private UrlCacheManager cacheManager;


    public Url searchUrl(String url) {

        try {
            if (!UrlValidatorService.isValidUrl(url)) {
                throw new IllegalArgumentException("Invalid URL: " + url);
            }

            Url cachedUrl = cacheManager.isUrlCached(url);
            if (cachedUrl != null) {
                log.info("Cache hit for URL: {}", url);
                
                // Flatten cached URL before returning
                Url flattenedCached = new Url(url);
                flattenedCached.setChildUrls(flattenUrls(cachedUrl));
                return flattenedCached;
            }

            log.info("Cache miss for URL: {}, starting crawl", url);
            Parser parser = ParseFactory.getParser("html", new BFSParsingStrategy());
            Url parsedChildUrls = parser.parse(url);

            if (parsedChildUrls != null) {
                cacheManager.cacheUrl(parsedChildUrls);  // Cache nested structure
                log.info("Successfully crawled and cached URL: {}", url);
                
                // Flatten before returning
                Url flattenedResult = new Url(url);
                flattenedResult.setChildUrls(flattenUrls(parsedChildUrls));
                return flattenedResult;
            } else {
                return new Url(url);
            }

        } catch (Exception e) {
            log.error("Error searching URL: {} - {}", url, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Flatten nested URL tree into a single list
     */
    private List<Url> flattenUrls(Url root) {
        List<Url> flattened = new ArrayList<>();
        Queue<Url> queue = new LinkedList<>();
        
        if (root.getChildUrls() != null) {
            queue.addAll(root.getChildUrls());
        }
        
        while (!queue.isEmpty()) {
            Url current = queue.poll();
            flattened.add(new Url(current.getUrl()));  // Add URL without nested children
            
            if (current.getChildUrls() != null) {
                queue.addAll(current.getChildUrls());
            }
        }
        
        return flattened;
    }
}
