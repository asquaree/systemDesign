package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;

import java.util.List;

public class ConcurrentParsingStrategy implements ParsingStrategy{
    @Override
    public Url parse(String urlString) {
        // Implement concurrent parsing logic here
        return new Url(urlString);
    }
}
