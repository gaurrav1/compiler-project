package com.cdproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SymbolEntry {
    private String name;
    private String type;
    private String scope;
    private int lineNumber;
}