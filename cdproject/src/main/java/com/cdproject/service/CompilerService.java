package com.cdproject.service;

import com.cdproject.grammer.Java8Lexer;
import com.cdproject.grammer.Java8Parser;
import com.cdproject.model.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompilerService {

    public LexicalAnalysisResponse analyzeLexically(String sourceCode) {
        LexicalAnalysisResponse response = new LexicalAnalysisResponse();
        List<TokenDto> tokens = new ArrayList<>();
        List<AnalysisError> errors = new ArrayList<>();

        CharStream input = CharStreams.fromString(sourceCode);
        Java8Lexer lexer = new Java8Lexer(input);

        // Custom error listener for lexical errors
        lexer.removeErrorListeners(); // Remove default error listeners
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                errors.add(new AnalysisError(line, "Lexical error at position " + charPositionInLine + ": " + msg));
            }
        });

        for (Token token : lexer.getAllTokens()) {
            if (token.getType() == Token.EOF) break;
            String symbolicName = lexer.getVocabulary().getSymbolicName(token.getType());
            String category = formatSymbolicName(symbolicName); // Format token category
            tokens.add(new TokenDto(token.getLine(), category, token.getText()));
        }


        response.setTokens(tokens);
        response.setErrors(errors);
        return response;
    }


    public SyntaxAnalysisResponse analyzeSyntax(String sourceCode) {
        SyntaxAnalysisResponse response = new SyntaxAnalysisResponse();
        List<AnalysisError> errors = new ArrayList<>();

        CharStream input = CharStreams.fromString(sourceCode);
        Java8Lexer lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);

        // Custom error listener to capture syntax errors
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                errors.add(new AnalysisError(line, "Syntax error at position " + charPositionInLine + ": " + msg));
            }
        });

        ParseTree tree = parser.compilationUnit(); // Parse the entire program

        if (errors.isEmpty()) {
            SymbolTableVisitor visitor = new SymbolTableVisitor();
            visitor.visit(tree);
            response.setSymbolTable(visitor.getSymbolTable());
            Map<String, String> flatSymbolTable = new HashMap<>();
            for (SymbolEntry entry : visitor.getSymbolTable()) {
                flatSymbolTable.put(entry.getName(), entry.getType());
            }
            SemanticAnalysisVisitor semanticVisitor = new SemanticAnalysisVisitor(flatSymbolTable);

            semanticVisitor.visit(tree);
            errors.addAll(semanticVisitor.getErrors());

            // Three-Address Code Generation
            if (errors.isEmpty()) {
                ThreeAddressCodeVisitor codeVisitor = new ThreeAddressCodeVisitor();
                codeVisitor.visit(tree);
                response.setThreeAddressCode(codeVisitor.getCode());
            }
        } else {
            response.setSymbolTable(new ArrayList<>());
        }

        response.setParseTree(buildParseTreeDTO(tree, parser));
        response.setErrors(errors);

        return response;
    }

    private ParseTreeDTO buildParseTreeDTO(ParseTree tree, Parser parser) {
        ParseTreeDTO dto = new ParseTreeDTO();
        if (tree instanceof TerminalNode) {
            TerminalNode node = (TerminalNode) tree;
            dto.setName(parser.getVocabulary().getSymbolicName(node.getSymbol().getType()));
            dto.setValue(node.getText());
        } else {
            RuleContext ctx = (RuleContext) tree;
            dto.setName(parser.getRuleNames()[ctx.getRuleIndex()]);
            for (int i = 0; i < tree.getChildCount(); i++) {
                dto.getChildren().add(buildParseTreeDTO(tree.getChild(i), parser));
            }
        }
        return dto;
    }

    private String formatSymbolicName(String symbolicName) {
        if (symbolicName == null) return "UNKNOWN";

        // List of Java keywords (in uppercase as they appear in the lexer's symbolic names)
        Set<String> keywords = Set.of(
                "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", "CHAR",
                "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", "ENUM",
                "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", "IMPLEMENTS",
                "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", "NEW",
                "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", "STATIC",
                "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", "THROWS",
                "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE"
        );

        if (keywords.contains(symbolicName)) {
            return "Keyword"; // Unified category for all keywords
        } else {
            // Format non-keyword tokens (e.g., "Identifier" -> "Identifier", "IntegerLiteral" -> "IntegerLiteral")
            return symbolicName.charAt(0) + symbolicName.substring(1).toLowerCase();
        }
    }
}