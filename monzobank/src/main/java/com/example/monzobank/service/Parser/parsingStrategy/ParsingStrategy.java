package com.example.monzobank.service.Parser.parsingStrategy;

import com.example.monzobank.entities.Url;

import java.util.List;

public interface ParsingStrategy {

    public Url parse(String urlString);
}
