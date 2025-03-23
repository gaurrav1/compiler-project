package com.cdproject.service;

import com.cdproject.dtos.LexicalAnalysisResponse;
import com.cdproject.dtos.SymbolEntryDto;
import com.cdproject.dtos.TokenDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class LexicalAnalyzerService {
    private static final List<String> KEYWORDS = List.of(
        "int", "float", "if", "else", "public", "class", 
        "void", "double", "String", "static"
    );
    
    private static final List<String> OPERATORS = List.of(
        "+", "-", "*", "/", "%", "="
    );
    
    private static final List<String> LOGICAL_OPERATORS = List.of(
        "<", ">", "<=", ">=", "==", "!="
    );
    
    private static final List<String> SPECIAL_SYMBOLS = List.of(
        "(", ")", "{", "}", ",", ";", ".", ":", "[", "]"
    );

    public LexicalAnalysisResponse analyzeCode(String sourceCode) {
        LexicalAnalysisResponse response = new LexicalAnalysisResponse();
        List<String> lines = new ArrayList<>();
        int lineNumber = 1;

        Scanner scanner = new Scanner(sourceCode);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                lineNumber++;
                continue;
            }

            String[] tokens = line.split("(?<=\\W)|(?=\\W)");
            for (String token : tokens) {
                if (token.trim().isEmpty()) continue;
                
                processToken(token, lineNumber, response);
                categorizeSymbol(token, lineNumber, response);
            }
            lineNumber++;
        }

        buildSymbolTable(response);
        return response;
    }

    private void processToken(String token, int lineNumber, LexicalAnalysisResponse response) {
        String category = determineTokenCategory(token);
        if (category != null) {
            response.getTokens().add(new TokenDto(lineNumber, category, token));
        }
    }

    private String determineTokenCategory(String token) {
        if (KEYWORDS.contains(token)) return "Keyword";
        if (OPERATORS.contains(token)) return "Operator";
        if (LOGICAL_OPERATORS.contains(token)) return "Logical Operator";
        if (SPECIAL_SYMBOLS.contains(token)) return "Special Symbol";
        if (token.matches("\\d+|\\d+\\.\\d+")) return "Numeric";
        if (token.matches("\".*\"")) return "String Literal";
        if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return "Identifier";
        return null;
    }

    private void categorizeSymbol(String token, int lineNumber, LexicalAnalysisResponse response) {
        if (determineTokenCategory(token) == "Identifier") {
            String type = "Unknown"; // You would need type inference logic here
            response.getSymbolEntries().add(new SymbolEntryDto(
                lineNumber, token, type, "Identifier"));
        }
    }

    private void buildSymbolTable(LexicalAnalysisResponse response) {
        response.getSymbolTable().put("Keywords", KEYWORDS);
        response.getSymbolTable().put("Operators", OPERATORS);
        response.getSymbolTable().put("Logical Operators", LOGICAL_OPERATORS);
        response.getSymbolTable().put("Special Symbols", SPECIAL_SYMBOLS);
        
        List<String> identifiers = response.getSymbolEntries().stream()
            .map(SymbolEntryDto::getName)
            .distinct()
            .collect(Collectors.toList());
        response.getSymbolTable().put("Identifiers", identifiers);
    }
}