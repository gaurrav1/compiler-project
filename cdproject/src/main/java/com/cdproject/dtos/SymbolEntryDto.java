package com.cdproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SymbolEntryDto {
    private int lineNumber;
    private String name;
    private String type;
    private String category;
}