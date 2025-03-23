package com.cdproject.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LexemeGenerator {
    private final List<String> tokens;
    private final List<String> symbols;

    public LexemeGenerator(String code) {
        tokens = new ArrayList<>();
        symbols = new ArrayList<>();
        processCode(code);
    }

    private void processCode(String code) {
        String[] words = code.split("\\s+|\\W+");
        for (String word : words) {
            if (word.matches("[0-9]+")) {
                tokens.add("Number: " + word);
            } else if (word.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                symbols.add("Identifier: " + word);
            }
        }
    }

}