package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;

import java.util.ArrayList;
import java.util.List;

public class BFSParsingStrategy implements ParsingStrategy{
    @Override
    public Url parse(String urlString) {
        // Implement BFS parsing logic here

        Url parsedUrl = new Url(urlString);
        System.out.println("Parsing using BFS strategy for URL: " + urlString);
        Url childUrl1 = new Url(urlString+"home");
        Url childUrl2 = new Url(urlString+"login");

        parsedUrl.setChildUrls(new ArrayList<>(List.of(childUrl1,childUrl2)));



        return parsedUrl;
    }
}
