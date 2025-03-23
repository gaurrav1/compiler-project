package com.cdproject.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class LexemeResponse {
    private final List<String> tokens;
    private final List<String> symbols;

    public LexemeResponse(List<String> tokens, List<String> symbols) {
        this.tokens = tokens;
        this.symbols = symbols;
    }

}