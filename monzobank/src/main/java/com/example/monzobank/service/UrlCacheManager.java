package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UrlCacheManager {

    Set<Url> cachedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());

    Url url1 = new Url("http://amex.com");

    Url childUrl1 = new Url("http://amex.com/child1");
    Url childUrl2 = new Url("http://amex.com/child2");

    // instance initializer block — valid place for statements
    {
        url1.setChildUrls(Arrays.asList(childUrl1, childUrl2));
        cachedUrls.add(url1);
    }


    public void cacheUrl(Url url) {
        cachedUrls.add(url);
    }
    
    public Url isUrlCached(String url) {
        System.out.println("Checking cache for URL: " + url);
        return cachedUrls.stream()
                .filter(cachedUrl -> cachedUrl.getUrl().equals(url))
                .findFirst()
                .orElse(null);
    }


}
