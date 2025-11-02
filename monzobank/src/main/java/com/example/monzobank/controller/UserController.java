package com.example.monzobank.controller;

import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/webCrawl/getChildUrls")
    public ResponseEntity<CrawlResponseModel> getChildUrlsForUser(@RequestParam String url,
                                                                  @RequestParam String userEmail) {
        log.info("Received request to crawl URL: {} for user: {}", url, userEmail);
        // suggestUrls(query); // Suggestion logic can be implemented here if needed
        return userService.searchUrl(url, userEmail);
    }

    @PostMapping("/webCrawl/visitUrl")
    public ResponseEntity<Boolean> visitUrl(@RequestParam String parentUrl, @RequestParam String childUrl,
                                            @RequestParam String userEmail) {
        log.info("Marking URL as visited - Parent: {}, Child: {}, User: {}", parentUrl, childUrl, userEmail);
        return userService.markUrlAsVisited(parentUrl, childUrl, userEmail);
    }

    @GetMapping("/webCrawl/suggestUrls")
    public ResponseEntity<List<String>> suggestUrls(@RequestParam String query) {
        log.info("Received URL suggestion request for query: {}", query);
        return userService.suggestUrls(query);
    }

}
