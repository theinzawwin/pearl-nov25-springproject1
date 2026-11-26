package com.pearl.nov25.springproj1.dto;

import com.pearl.nov25.springproj1.models.User;

public record LoginResponse(
    String token,
    String refreshToken,
    Long expiration,
    User user
) {}
