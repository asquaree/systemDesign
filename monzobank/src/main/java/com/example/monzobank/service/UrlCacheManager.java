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

    private static final int MAX_CACHE_SIZE = 1000;

    // Cache storage
    private final Map<String, Url> cachedUrls = new ConcurrentHashMap<>();

    // LRU queue: least recently used at front, most recently used at back
    private final Deque<String> lruQueue = new LinkedList<>();

    // Initialize test cache data
    {
        Url googleUrl = new Url("https://www.google.com/");

        Url googleSearch = new Url("https://www.google.com/search");
        Url googleMaps = new Url("https://www.google.com/maps");
        Url googleImages = new Url("https://www.google.com/imghp");
        Url googleNews = new Url("https://www.google.com/news");

        googleUrl.setChildUrls(Arrays.asList(googleSearch, googleMaps, googleImages, googleNews));
        cacheUrl(googleUrl);
    }

    public synchronized void cacheUrl(Url url) {
        String urlString = url.getUrl();

        if (cachedUrls.containsKey(urlString)) {
            lruQueue.remove(urlString);  // Remove old position
        } else {
            if (cachedUrls.size() >= MAX_CACHE_SIZE) {
                evictLeastRecentlyUsed();
            }
        }

        // Add/Update in cache
        cachedUrls.put(urlString, url);

        // Add to end of queue (most recently used)
        lruQueue.addLast(urlString);

        log.debug("Cached URL: {} (Cache size: {})", urlString, cachedUrls.size());
    }

    public synchronized Url isUrlCached(String url) {
        log.debug("Checking cache for URL: {}", url);

        Url cachedUrl = cachedUrls.get(url);

        if (cachedUrl != null) {
            lruQueue.remove(url);
            lruQueue.addLast(url);
            log.debug("Cache HIT: {} (moved to end of LRU queue)", url);
        } else {
            log.debug("Cache MISS: {}", url);
        }

        return cachedUrl;
    }

    private void evictLeastRecentlyUsed() {
        // Remove from front of queue (least recently used)
        String lruUrl = lruQueue.pollFirst();

        if (lruUrl != null) {
            cachedUrls.remove(lruUrl);
            log.info("Evicted LRU URL: {} (Cache was full at {} entries)",
                    lruUrl, MAX_CACHE_SIZE);
        }
    }

}