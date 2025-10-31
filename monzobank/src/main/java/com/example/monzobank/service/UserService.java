package com.example.monzobank.service;

import com.example.monzobank.entities.Url;
import com.example.monzobank.entities.User;
import com.example.monzobank.mapper.ChildUrlResponseMapper;
import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UrlSearchService urlSearchService;

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<CrawlResponseModel> searchUrl(String url, String userEmail) {

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
    }
}
