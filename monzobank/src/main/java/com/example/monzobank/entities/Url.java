package com.example.monzobank.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Url {

    private String url;
    private List<Url> childUrls;
    private int nestingLevel;

    public Url(String url) {
        this.url = url;
    }
}
