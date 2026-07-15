package com.example.demo.user;


public record AuthResponseDto(
        String token,
        String username,
        String role
) { }

