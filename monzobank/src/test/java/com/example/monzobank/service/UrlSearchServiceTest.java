package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlSearchServiceTest {

    @Mock
    private UrlCacheManager cacheManager;

    @InjectMocks
    private UrlSearchService urlSearchService;

    @Test
    void testSearchUrl_InvalidUrl() {
        Url result = urlSearchService.searchUrl("invalid-url");
        
        assertNull(result);
        verify(cacheManager, never()).isUrlCached(anyString());
    }

    @Test
    void testSearchUrl_CacheHit() {
        String url = "https://example.com";
        Url cachedUrl = new Url(url);
        cachedUrl.setChildUrls(List.of(new Url("https://example.com/page1")));
        
        when(cacheManager.isUrlCached(url)).thenReturn(cachedUrl);
        
        Url result = urlSearchService.searchUrl(url);
        
        assertNotNull(result);
        assertEquals(url, result.getUrl());
        verify(cacheManager, times(1)).isUrlCached(url);
        verify(cacheManager, never()).cacheUrl(any());
    }

    @Test
    void testSearchUrl_NullUrl() {
        Url result = urlSearchService.searchUrl(null);
        
        assertNull(result);
    }
}
