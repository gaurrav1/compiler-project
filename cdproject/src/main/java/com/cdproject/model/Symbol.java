package com.cdproject.model;

import lombok.Getter;

@Getter
public class Symbol {
    private final String name;
    private final String category;

    public Symbol(String name, String category) {
        this.name = name;
        this.category = category;
    }

}