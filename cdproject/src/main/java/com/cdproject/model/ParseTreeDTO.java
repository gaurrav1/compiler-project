// ParseTreeDTO.java
package com.cdproject.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ParseTreeDTO {
    private String name;
    private String value;
    private List<ParseTreeDTO> children = new ArrayList<>();
}