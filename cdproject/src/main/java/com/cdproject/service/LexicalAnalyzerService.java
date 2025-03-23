package com.cdproject.service;

import com.cdproject.dtos.AnalysisError;
import com.cdproject.dtos.LexicalAnalysisResponse;
import com.cdproject.dtos.SymbolEntryDto;
import com.cdproject.dtos.TokenDto;
import org.springframework.stereotype.Service;

import java.util.*;
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
        List<String> declaredVariables = new ArrayList<>();
        final String[] currentType = { null };
        int lineNumber = 1;
        boolean inLiteral = false;
        StringBuilder literalBuffer = new StringBuilder();

        Scanner scanner = new Scanner(sourceCode);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                lineNumber++;
                continue;
            }

            String[] tokens = line.split(
                    "(?=(\\s|\\W))|(?<=(\\s|\\W))|(?<=\\d)(?=\\.)|(?<=\\.)(?=\\d)"
            );

            for (String token : tokens) {
                token = token.trim();
                if (token.isEmpty()) continue;

                if (inLiteral) {
                    literalBuffer.append(token);
                    if (token.contains("\"")) {
                        processLiteral(literalBuffer.toString(), lineNumber, response);
                        literalBuffer.setLength(0);
                        inLiteral = false;
                    }
                    continue;
                }

                if (token.startsWith("\"")) {
                    inLiteral = true;
                    literalBuffer.append(token);
                    continue;
                }

                processToken(token, lineNumber, response, declaredVariables, currentType);

                // Reset type after semicolon
                if (currentType[0] != null && token.equals(";")) {
                    currentType[0] = null;
                }
            }
            lineNumber++;
        }

        validateVariables(declaredVariables, response);
        buildSymbolTable(response);
        return response;
    }

    private void processToken(String token, int lineNumber,
                              LexicalAnalysisResponse response,
                              List<String> declaredVariables,
                              String[] currentType) {
        String category = determineTokenCategory(token);

        if (category == null) {
            if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                category = "Identifier";
            } else {
                response.getErrors().add(new AnalysisError(lineNumber, "Invalid token: " + token));
                return;
            }
        }

        response.getTokens().add(new TokenDto(lineNumber, category, token));

        // Handle type declarations
        if (category.equals("Keyword") && isTypeKeyword(token)) {
            currentType[0] = token;
        }
        // Handle variable declarations
        else if (category.equals("Identifier") && currentType[0] != null) {
            if (!declaredVariables.contains(token)) {
                declaredVariables.add(token);
                response.getSymbolEntries().add(new SymbolEntryDto(
                        lineNumber, token, currentType[0], "Variable"
                ));
            }
        }
    }

    private void processLiteral(String literal, int lineNumber,
                                LexicalAnalysisResponse response) {
        response.getTokens().add(new TokenDto(
                lineNumber,
                "String Literal",
                literal.replaceAll("^\"|\"$", "")
        ));
    }

    private void validateVariables(List<String> declaredVariables,
                                   LexicalAnalysisResponse response) {
        response.getTokens().stream()
                .filter(t -> t.getCategory().equals("Identifier"))
                .forEach(t -> {
                    if (!declaredVariables.contains(t.getValue()) &&
                            !isReservedWord(t.getValue())) {
                        response.getErrors().add(new AnalysisError(
                                t.getLineNumber(),
                                "Undeclared variable: " + t.getValue()
                        ));
                    }
                });
    }

    private String determineTokenCategory(String token) {
        if (KEYWORDS.contains(token)) return "Keyword";
        if (OPERATORS.contains(token)) return "Operator";
        if (LOGICAL_OPERATORS.contains(token)) return "Logical Operator";
        if (SPECIAL_SYMBOLS.contains(token)) return "Special Symbol";
        if (token.matches("\\d+\\.\\d+")) return "Float";
        if (token.matches("\\d+")) return "Integer";
        if (token.matches("\".*\"")) return "String Literal";
        if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) return "Identifier";
        return null;
    }

    private boolean isTypeKeyword(String token) {
        return Set.of("int", "float", "double", "String").contains(token);
    }

    private boolean isReservedWord(String token) {
        return KEYWORDS.contains(token) ||
                OPERATORS.contains(token) ||
                LOGICAL_OPERATORS.contains(token);
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
