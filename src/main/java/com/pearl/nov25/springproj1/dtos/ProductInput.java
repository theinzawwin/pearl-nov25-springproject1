package com.pearl.nov25.springproj1.dtos;

public record ProductInput(String name,String barcode,long brandId,String photo,boolean activeStatus,String description) {
}
