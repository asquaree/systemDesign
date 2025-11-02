package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UrlCacheManager {

    private final ConcurrentHashMap<String, Url> cachedUrls = new ConcurrentHashMap<>();

    // Initialize test cache data
    {
        // Google URL with nested children
        Url googleUrl = new Url("https://www.google.com/");
        
        Url googleSearch = new Url("https://www.google.com/search");
        Url googleMaps = new Url("https://www.google.com/maps");
        Url googleImages = new Url("https://www.google.com/imghp");
        Url googleNews = new Url("https://www.google.com/news");
        
        googleUrl.setChildUrls(Arrays.asList(googleSearch, googleMaps, googleImages, googleNews));
        cachedUrls.put(googleUrl.getUrl(),googleUrl);
    }


    public void cacheUrl(Url url) {
        cachedUrls.put(url.getUrl(),url);
    }
    
    public Url isUrlCached(String url) {
        log.debug("Checking cache for URL: {}", url);
        return cachedUrls.get(url);
    }
}
