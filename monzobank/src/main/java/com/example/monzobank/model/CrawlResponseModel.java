package com.example.monzobank.model;

import com.example.monzobank.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class CrawlResponseModel {

    String url;

    List<UrlVistedModel> childUrls;

    User user;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UrlVistedModel> getChildUrls() {
        return childUrls;
    }

    public void setChildUrls(List<UrlVistedModel> childUrls) {
        this.childUrls = childUrls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
