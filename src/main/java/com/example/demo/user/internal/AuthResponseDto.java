package com.example.demo.user.internal;


public record AuthResponseDto(
        String token,
        String username,
        String role
) { }

