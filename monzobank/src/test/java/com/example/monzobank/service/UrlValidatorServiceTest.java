package com.example.monzobank.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorServiceTest {

    @Test
    void testIsValidUrl_ValidHttpUrl() {
        assertTrue(UrlValidatorService.isValidUrl("http://example.com"));
        assertTrue(UrlValidatorService.isValidUrl("https://en.wikipedia.org/wiki/Java"));
    }

    @Test
    void testIsValidUrl_InvalidUrl() {
        assertFalse(UrlValidatorService.isValidUrl("not-a-url"));
        assertFalse(UrlValidatorService.isValidUrl(""));
        assertFalse(UrlValidatorService.isValidUrl(null));
    }

    @Test
    void testIsValidLink_ValidLinks() {
        assertTrue(UrlValidatorService.isValidLink("https://example.com"));
        assertTrue(UrlValidatorService.isValidLink("http://test.org/page"));
    }

    @Test
    void testIsValidLink_InvalidLinks() {
        // Contains hash
        assertFalse(UrlValidatorService.isValidLink("https://example.com#section"));
        
        // File extensions
        assertFalse(UrlValidatorService.isValidLink("https://example.com/file.pdf"));
        assertFalse(UrlValidatorService.isValidLink("https://example.com/image.jpg"));
        
        // Null or empty
        assertFalse(UrlValidatorService.isValidLink(null));
        assertFalse(UrlValidatorService.isValidLink(""));
    }

    @Test
    void testExtractBaseDomain() {
        assertEquals("example.com", UrlValidatorService.extractBaseDomain("https://example.com"));
        assertEquals("google.com", UrlValidatorService.extractBaseDomain("http://maps.google.com/search"));
    }

    @Test
    void testExtractBaseDomain_InvalidUrl() {
        assertEquals("", UrlValidatorService.extractBaseDomain("not-a-url"));
    }

    @Test
    void testIsInternalLink_SameDomain() {
        String baseDomain = "wikipedia.org";
        
        assertTrue(UrlValidatorService.isInternalLink("https://en.wikipedia.org", baseDomain));
        assertTrue(UrlValidatorService.isInternalLink("https://wikipedia.org/page", baseDomain));
    }

    @Test
    void testIsInternalLink_DifferentDomain() {
        String baseDomain = "wikipedia.org";
        
        assertFalse(UrlValidatorService.isInternalLink("https://google.com", baseDomain));
        assertFalse(UrlValidatorService.isInternalLink("https://facebook.com", baseDomain));
    }

    @Test
    void testIsInternalLink_InvalidUrl() {
        assertFalse(UrlValidatorService.isInternalLink("invalid-url", "example.com"));
    }
}
