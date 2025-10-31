package com.example.monzobank.service.Parser;

import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;

public class JsonParser extends Parser{
    public JsonParser(ParsingStrategy parsingStrategy) {
        super(parsingStrategy);
        this.parseType = "json";

        System.out.println("JsonParser created with strategy: " + parsingStrategy.getClass().getSimpleName());
    }
}
