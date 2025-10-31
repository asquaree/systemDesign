package com.example.monzobank.service.Parser;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;

import java.util.List;

public abstract class Parser {

    protected String parseType;
    protected ParsingStrategy parsingStrategy;

    public Parser(ParsingStrategy parsingStrategy){
        this.parsingStrategy = parsingStrategy;
    }

    public Url parse(String urlString) {
        System.out.println("Using parse type: " + parseType);
        System.out.println("Parsing Strategy: " + parsingStrategy.getClass().getSimpleName());
        return parsingStrategy.parse(urlString, this);
    }
    
    // Each parser (HTML/JSON/XML/etc) implements how to extract links from their format
    public abstract List<String> extractLinks(String urlString);
}
