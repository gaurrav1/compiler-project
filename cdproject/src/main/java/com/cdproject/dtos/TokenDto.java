package com.cdproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private int lineNumber;
    private String category;
    private String value;
}