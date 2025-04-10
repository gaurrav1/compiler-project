package com.cdproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SyntaxAnalysisResponse {
    private ParseTreeDTO parseTree; // Changed from String
    private List<SymbolEntry> symbolTable; // Changed from Map
    private List<AnalysisError> errors;
    private List<String> threeAddressCode;
}