package com.example.monzobank.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class Url {

    private String url;
    private List<Url> childUrls;
    private int nestingLevel;

    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Url> getChildUrls() {
        return childUrls;
    }

    public void setChildUrls(List<Url> childUrls) {
        this.childUrls = childUrls;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
    }
}
