package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.ParseFactory;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.Parser.parsingStrategy.BFSParsingStrategy;
import com.example.monzobank.service.Parser.parsingStrategy.DFSParsingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UrlSearchService {

    @Autowired
    UrlValidatorService urlValidatorService;

    @Autowired
    private UrlCacheManager cacheManager;


    public Url searchUrl(String url) {

        try {
            if (!urlValidatorService.isValidUrl(url)) {
                throw new IllegalArgumentException("Invalid URL: " + url);
            }

            Url cachedUrl = cacheManager.isUrlCached(url);
            if (cachedUrl != null) {
                System.out.println("URL found in cache: " + url);
                return cachedUrl;
            }

            Parser parser = ParseFactory.getParser("html", new BFSParsingStrategy(urlValidatorService));
            Url parsedChildUrls = parser.parse(url);

            if (parsedChildUrls != null) {
                cacheManager.cacheUrl(parsedChildUrls);
                return parsedChildUrls;
            } else {
                return new Url(url);
            }

        } catch (Exception e) {
            System.err.println("Error searching URL: " + e.getMessage());
            return null;
        }
    }
}
