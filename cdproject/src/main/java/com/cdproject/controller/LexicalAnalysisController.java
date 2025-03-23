package com.cdproject.controller;

import com.cdproject.dtos.LexicalAnalysisResponse;
import com.cdproject.service.LexicalAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lexical-analysis")
@CrossOrigin(origins = "http://localhost:5050")
public class LexicalAnalysisController {
    private final LexicalAnalyzerService analyzerService;

    public LexicalAnalysisController(LexicalAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping
    public ResponseEntity<LexicalAnalysisResponse> analyzeCode(@RequestBody String sourceCode) {
        return ResponseEntity.ok(analyzerService.analyzeCode(sourceCode));
    }
}