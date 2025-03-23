package com.cdproject.dtos;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LexicalAnalysisResponse {
    private final List<TokenDto> tokens = new ArrayList<>();
    private final Map<String, List<String>> symbolTable = new HashMap<>();
    private final List<SymbolEntryDto> symbolEntries = new ArrayList<>();
    private final List<AnalysisError> errors = new ArrayList<>();
}