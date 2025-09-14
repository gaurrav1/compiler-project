package com.cdproject.controller;

import com.cdproject.model.LexicalAnalysisResponse;
import com.cdproject.model.SyntaxAnalysisResponse;
import com.cdproject.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compiler")
@CrossOrigin(origins = {"http://localhost:5500/"})
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/lexical")
    public ResponseEntity<LexicalAnalysisResponse> analyzeLexically(@RequestBody String sourceCode) {
        return ResponseEntity.ok(compilerService.analyzeLexically(sourceCode));
    }

    @PostMapping("/syntax")
    public ResponseEntity<SyntaxAnalysisResponse> analyzeSyntax(@RequestBody String sourceCode) {
        return ResponseEntity.ok(compilerService.analyzeSyntax(sourceCode));
    }
}