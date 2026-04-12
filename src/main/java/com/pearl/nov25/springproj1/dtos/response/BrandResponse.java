package com.pearl.nov25.springproj1.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse implements Serializable {

    private Long id;
    private String name;
    private boolean status;
}
