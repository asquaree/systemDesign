package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;
import com.example.monzobank.service.UrlValidatorService;

import java.util.*;

public class DFSParsingStrategy implements ParsingStrategy {
    
    private static final int MAX_LINKS = 50;

    public DFSParsingStrategy() {
    }

    @Override
    public Url parse(String urlString, Parser parser, int maxDepth) {

        // Implement DFS parsing logic here
        return new Url(urlString);

    }
    

}
