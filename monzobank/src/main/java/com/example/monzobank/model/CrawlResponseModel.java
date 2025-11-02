package com.example.monzobank.model;

import com.example.monzobank.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawlResponseModel {

    private String url;
    private List<UrlVistedModel> childUrls;
    private User user;
}
