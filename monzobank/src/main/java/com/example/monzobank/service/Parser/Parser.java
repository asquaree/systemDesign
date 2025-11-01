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

    /**
     * Parse URL with unlimited depth (crawl all levels until no more links)
     */
    public Url parse(String urlString) {
        System.out.println("Using parse type: " + parseType);
        System.out.println("Parsing Strategy: " + parsingStrategy.getClass().getSimpleName());
        System.out.println("Depth: UNLIMITED");
        return parsingStrategy.parse(urlString, this, Integer.MAX_VALUE);
    }
    
    /**
     * Parse URL with specified depth level
     * @param urlString - URL to parse
     * @param depth - How many levels deep to crawl (1 = only immediate children, 2 = children + grandchildren, etc.)
     */
    public Url parse(String urlString, int depth) {
        System.out.println("Using parse type: " + parseType);
        System.out.println("Parsing Strategy: " + parsingStrategy.getClass().getSimpleName());
        System.out.println("Depth: " + depth);
        return parsingStrategy.parse(urlString, this, depth);
    }
    
    // Each parser (HTML/JSON/XML/etc) implements how to extract links from their format
    public abstract List<String> extractLinks(String urlString);
}
