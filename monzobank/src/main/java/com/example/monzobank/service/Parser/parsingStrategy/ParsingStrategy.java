package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;
import com.example.monzobank.service.Parser.Parser;

public interface ParsingStrategy {

    Url parse(String urlString, Parser parser, int depth);
}
