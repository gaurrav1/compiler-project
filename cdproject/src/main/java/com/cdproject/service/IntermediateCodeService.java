package com.cdproject.service;

import com.cdproject.utils.ThreeAddressCode;
import org.springframework.stereotype.Service;

@Service
public class IntermediateCodeService {
    public String generateIntermediateCode(String code) {
        return ThreeAddressCode.generateCode(code);
    }
}