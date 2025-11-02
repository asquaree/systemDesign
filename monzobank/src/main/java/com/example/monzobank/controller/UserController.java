package com.example.monzobank.controller;

import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/webCrawl/getChildUrls")
    public ResponseEntity<CrawlResponseModel> getChildUrlsForUser(@RequestParam String url,
                                                @RequestParam String userEmail) {
        return userService.searchUrl(url, userEmail);
    }

    @PostMapping("/webCrawl/visitUrl")
    public ResponseEntity<Boolean> visitUrl(@RequestParam String parentUrl,@RequestParam String childUrl,
                                                @RequestParam String userEmail) {
        return userService.markUrlAsVisited(parentUrl,childUrl, userEmail);
    }



}
