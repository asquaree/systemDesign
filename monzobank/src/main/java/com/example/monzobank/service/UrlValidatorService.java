package com.example.monzobank.service;

import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class UrlValidatorService {


    public static boolean isValidUrl(String url) {

        if(!isValidDomainFormat(url)) {
            return false;
        } else {
            return isValidUrlSyntax(url);
        }
    }

    private static boolean isValidUrlSyntax(String url) {
        try {
            new URL(url).toURI(); // validates both syntax & structure
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static final String DOMAIN_REGEX =
            "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/.*)?$";

    public static boolean isValidDomainFormat(String url) {
        return url.matches(DOMAIN_REGEX);
    }

    public static boolean isValidLink(String url) {
        return url != null && 
               !url.isEmpty() && 
               (url.startsWith("http://") || url.startsWith("https://")) &&
               !url.contains("#") &&
               !url.endsWith(".pdf") &&
               !url.endsWith(".jpg") &&
               !url.endsWith(".png") &&
               !url.endsWith(".gif") &&
               isValidUrl(url);
    }

    /**
     * Extract base domain from URL (e.g., "wikipedia.org" from "https://en.wikipedia.org/wiki/...")
     */
    public static String extractBaseDomain(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();

            // Remove "www." prefix if present
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            // Extract base domain (e.g., "wikipedia.org" from "en.wikipedia.org")
            String[] parts = host.split("\\.");
            if (parts.length >= 2) {
                return parts[parts.length - 2] + "." + parts[parts.length - 1];
            }

            return host;
        } catch (Exception e) {
            System.err.println("Error extracting base domain from: " + urlString);
            return "";
        }
    }

    /**
     * Check if link is internal (same domain as base)
     * Examples:
     * - wikipedia.org checking en.wikipedia.org → true
     * - wikipedia.org checking ja.wikipedia.org → true
     * - wikipedia.org checking facebook.com → false
     */
    public static boolean isInternalLink(String linkUrl, String baseDomain) {
        try {
            URL url = new URL(linkUrl);
            String host = url.getHost();

            // Remove "www." prefix if present
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            // Check if host contains the base domain
            return host.endsWith(baseDomain) || host.equals(baseDomain);
        } catch (Exception e) {
            return false;
        }
    }


}

