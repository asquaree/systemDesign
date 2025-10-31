package com.example.monzobank.service;

import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class UrlValidatorService {


    public boolean isValidUrl(String url) {

        if(!isValidDomainFormat(url)) {
            return false;
        } else {
            return isValidUrlSyntax(url);
        }
    }

    private boolean isValidUrlSyntax(String url) {
        try {
            new URL(url).toURI(); // validates both syntax & structure
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static final String DOMAIN_REGEX =
            "^(https?://)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/.*)?$";

    public boolean isValidDomainFormat(String url) {
        return url.matches(DOMAIN_REGEX);
    }

    public boolean isValidLink(String url) {
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
}

