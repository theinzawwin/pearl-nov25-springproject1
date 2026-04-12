package com.pearl.nov25.springproj1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInput {
    private String name;
    private String code;
    private boolean status;
    private String description;
    private Long brandId;
}
