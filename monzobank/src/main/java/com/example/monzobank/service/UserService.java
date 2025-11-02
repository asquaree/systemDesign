package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import com.example.monzobank.entities.User;
import com.example.monzobank.mapper.ChildUrlResponseMapper;
import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
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
                log.warn("User not found: {}", userEmail);
                return ResponseEntity.notFound().build();
            }

            Url urlWithChildren = urlSearchService.searchUrl(url);
            
            if (urlWithChildren == null) {
                log.warn("No child URLs found for: {}", url);
                return ResponseEntity.noContent().build();
            }

            CrawlResponseModel crawlResponseModel = ChildUrlResponseMapper.mapChildUrlsToResponseModel(url, urlWithChildren, user);
            log.info("Successfully crawled URL: {} with {} child URLs for user: {}", 
                    url, crawlResponseModel.getChildUrls().size(), userEmail);
            return ResponseEntity.ok(crawlResponseModel);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid URL format: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error in searchUrl for URL: {} - {}", url, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Boolean> markUrlAsVisited(String parentUrl, String childUrl, String userEmail) {
        try {
            System.out.println("Visting URL: Parent - " + parentUrl + ", Child - " + childUrl + ", User - " + userEmail);
            if (parentUrl == null || childUrl == null || userEmail == null) {
                return ResponseEntity.badRequest().body(false);
            }
            
            Boolean result = userRepository.markUrlsAsVisited(parentUrl, childUrl, userEmail);
            if (result) {
                log.info("Successfully marked URL as visited for user: {}", userEmail);
            }
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error marking URL as visited: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(false);
        }
    }

    public ResponseEntity<java.util.List<String>> suggestUrls(String query) {
        return UrlSuggestService.suggestUrls(query);
    }

}
