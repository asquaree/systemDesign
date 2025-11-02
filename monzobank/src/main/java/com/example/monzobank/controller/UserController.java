package com.example.monzobank.controller;

import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/webCrawl/getChildUrls")
    public ResponseEntity<CrawlResponseModel> getChildUrlsForUser(@RequestParam String url,
                                                                  @RequestParam String userEmail) {
        log.info("Received request to crawl URL: {} for user: {}", url, userEmail);
        return userService.searchUrl(url, userEmail);
    }

    @PostMapping("/webCrawl/visitUrl")
    public ResponseEntity<Boolean> visitUrl(@RequestParam String parentUrl, @RequestParam String childUrl,
                                            @RequestParam String userEmail) {
        log.info("Marking URL as visited - Parent: {}, Child: {}, User: {}", parentUrl, childUrl, userEmail);
        return userService.markUrlAsVisited(parentUrl, childUrl, userEmail);
    }


}
