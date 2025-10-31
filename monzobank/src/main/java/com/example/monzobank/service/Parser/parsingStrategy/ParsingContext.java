package com.example.monzobank.service.Parser.parsingStrategy;

public class ParsingContext {

    private ParsingStrategy parsingStrategy;

    public void setParsingStrategy(ParsingStrategy parsingStrategy){
        this.parsingStrategy = parsingStrategy;
    }

    public void apply() {
        parsingStrategy.parse("url");
    }

}
