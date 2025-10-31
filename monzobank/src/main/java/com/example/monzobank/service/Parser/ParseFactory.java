package com.example.monzobank.service.Parser;

import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;

public class ParseFactory {
    public static Parser getParser(String type, ParsingStrategy parsingStrategy){
        if(type.equalsIgnoreCase("json")){
            return new JsonParser(parsingStrategy);
        } else if(type.equalsIgnoreCase("html")){
            return new HtmlParser(parsingStrategy);
        }
        throw new IllegalArgumentException("Unknown parser type: " + type);
    }
}

