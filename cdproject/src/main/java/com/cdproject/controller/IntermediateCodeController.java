package com.cdproject.controller;

import com.cdproject.dtos.CodeRequest;
import com.cdproject.service.IntermediateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/intermediate")
public class IntermediateCodeController {
    @Autowired
    private IntermediateCodeService intermediateCodeService;

    @PostMapping("/generate")
    public String generateIntermediateCode(@RequestBody CodeRequest request) {
        return intermediateCodeService.generateIntermediateCode(request.getCode());
    }
}