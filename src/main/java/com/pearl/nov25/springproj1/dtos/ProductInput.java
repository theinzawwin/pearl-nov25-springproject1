package com.pearl.nov25.springproj1.dtos;

public record ProductInput(String name,String barcode,Long brandId,String photo,boolean activeStatus,String description) {
}
