package com.pearl.nov25.springproj1.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;

    private String name;
    private String code;
    private String description;
    private boolean status;
    private Long brandId;
    private String brandName;

}
