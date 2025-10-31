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

        List<Url> childUrls = urlWithChildren.getChildUrls();
        Map<String, LocalDateTime> visitedUrls = user.getUrlLastAccessTime().get(url);

        CrawlResponseModel childUrlResponse = new CrawlResponseModel();

        List<UrlVistedModel> childUrlModels = new ArrayList<>();


        for (Url childUrl : childUrls) {

            UrlVistedModel urlVistedModel = new UrlVistedModel();
            urlVistedModel.setUrl(childUrl.getUrl());

            if(visitedUrls!= null) {
                if(visitedUrls.containsKey(childUrl.getUrl())) {
                    urlVistedModel.setVisited(true);
                    urlVistedModel.setLastAccessTime(visitedUrls.get(childUrl.getUrl()));
                } else {
                    urlVistedModel.setVisited(false);
                }
            }

            childUrlModels.add(urlVistedModel);

        }

        childUrlResponse.setUrl(url);
        childUrlResponse.setChildUrls(childUrlModels);
        childUrlResponse.setUser(user);

        return childUrlResponse;

    }
}
