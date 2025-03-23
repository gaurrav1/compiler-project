package com.cdproject.controller;

import com.cdproject.dtos.CodeRequest;
import com.cdproject.dtos.LexemeResponse;
import com.cdproject.service.LexemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lexemes")
@CrossOrigin(origins = "http://localhost:5050")
public class LexemeController {
    @Autowired
    private LexemeService lexemeService;

    @PostMapping("/generate")
    public LexemeResponse generateLexemes(@RequestBody CodeRequest request) {
        return lexemeService.generateLexemes(request.getCode());
    }
}