package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import com.example.monzobank.entities.User;
import com.example.monzobank.mapper.ChildUrlResponseMapper;
import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    UrlSearchService urlSearchService;

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<CrawlResponseModel> searchUrl(String url, String userEmail) {
        try {
            if (url == null || url.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            User user = userRepository.findByEmail(userEmail);
            
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            Url urlWithChildren = urlSearchService.searchUrl(url);
            
            if (urlWithChildren == null) {
                return ResponseEntity.noContent().build();
            }

            CrawlResponseModel crawlResponseModel = ChildUrlResponseMapper.mapChildUrlsToResponseModel(url, urlWithChildren, user);
            return ResponseEntity.ok(crawlResponseModel);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URL format: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error in searchUrl: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Boolean> markUrlAsVisited(String parentUrl, String childUrl, String userEmail) {
        try {
            if (parentUrl == null || childUrl == null || userEmail == null) {
                return ResponseEntity.badRequest().body(false);
            }
            
            Boolean result = userRepository.markUrlsAsVisited(parentUrl, childUrl, userEmail);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            System.err.println("Error marking URL as visited: " + e.getMessage());
            return ResponseEntity.internalServerError().body(false);
        }
    }
}
