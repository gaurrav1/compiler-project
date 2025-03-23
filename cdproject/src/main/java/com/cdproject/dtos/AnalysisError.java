package com.cdproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalysisError {
    private int lineNumber;
    private String message;
}