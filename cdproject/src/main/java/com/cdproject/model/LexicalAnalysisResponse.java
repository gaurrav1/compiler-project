package com.cdproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LexicalAnalysisResponse {
    private List<TokenDto> tokens;
    private List<AnalysisError> errors;
}