package com.example.monzobank.service.Parser;

import com.example.monzobank.service.Parser.parsingStrategy.ParsingStrategy;

public class HtmlParser extends Parser {
    public HtmlParser(ParsingStrategy parsingStrategy) {
        super(parsingStrategy);
        this.parseType = "html";
    }


}
