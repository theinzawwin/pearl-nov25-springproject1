package com.pearl.nov25.springproj1.dtos;

public record ProductInput(String name, String barCode, String photo, Boolean activeStatus, Long brandId) {
}
