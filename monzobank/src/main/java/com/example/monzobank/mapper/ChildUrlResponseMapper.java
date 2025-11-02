package com.example.monzobank.mapper;

import com.example.monzobank.entities.Url;
import com.example.monzobank.entities.User;
import com.example.monzobank.model.CrawlResponseModel;
import com.example.monzobank.model.UrlVistedModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChildUrlResponseMapper {

    public static CrawlResponseModel mapChildUrlsToResponseModel(String url, Url urlWithChildren, User user) {
        CrawlResponseModel childUrlResponse = new CrawlResponseModel();
        List<UrlVistedModel> childUrlModels = new ArrayList<>();

        // Null safety checks
        if (urlWithChildren == null || urlWithChildren.getChildUrls() == null) {
            childUrlResponse.setUrl(url);
            childUrlResponse.setChildUrls(childUrlModels);
            childUrlResponse.setUser(user);
            return childUrlResponse;
        }

        List<Url> childUrls = urlWithChildren.getChildUrls();
        Map<String, LocalDateTime> visitedUrls = null;
        
        if (user != null && user.getUrlLastAccessTime() != null) {
            visitedUrls = user.getUrlLastAccessTime().get(url);
        }

        for (Url childUrl : childUrls) {
            if (childUrl == null || childUrl.getUrl() == null) {
                continue;
            }
            
            UrlVistedModel urlVistedModel = new UrlVistedModel();
            urlVistedModel.setUrl(childUrl.getUrl());

            if (visitedUrls != null && visitedUrls.containsKey(childUrl.getUrl())) {
                urlVistedModel.setVisited(true);
                urlVistedModel.setLastAccessTime(visitedUrls.get(childUrl.getUrl()));
            } else {
                urlVistedModel.setVisited(false);
                urlVistedModel.setLastAccessTime(null);
            }

            childUrlModels.add(urlVistedModel);
        }

        childUrlResponse.setUrl(url);
        childUrlResponse.setChildUrls(childUrlModels);
        childUrlResponse.setUser(user);

        return childUrlResponse;
    }
}
