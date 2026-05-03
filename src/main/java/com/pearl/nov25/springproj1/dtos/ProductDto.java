package com.pearl.nov25.springproj1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String barcode;
    private boolean activeStatus;
    private Long brandId;
    private String brandName;

}
