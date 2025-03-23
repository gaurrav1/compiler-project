package com.cdproject.service;

import com.cdproject.dtos.LexemeResponse;
import com.cdproject.utils.LexemeGenerator;
import org.springframework.stereotype.Service;

@Service
public class LexemeService {
    public LexemeResponse generateLexemes(String code) {
        LexemeGenerator generator = new LexemeGenerator(code);
        return new LexemeResponse(generator.getTokens(), generator.getSymbols());
    }
}