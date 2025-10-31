package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;

import java.util.List;

public class ConcurrentParsingStrategy implements ParsingStrategy{
    @Override
    public Url parse(String urlString, Parser parser) {
        // Implement concurrent parsing logic here
        return new Url(urlString);
    }
}
