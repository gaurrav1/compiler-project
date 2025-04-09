package com.cdproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnalysisError {
    private int lineNumber;
    private int charPosition; // Uncommented
    private String message;
    private String type; // e.g., "Lexical", "Syntax", "Semantic"

    public AnalysisError(int line, String s) {
        this.lineNumber = line;
        this.message = s;
        this.charPosition = -1; // Default value for charPosition
        this.type = "General"; // Default type
    }
}