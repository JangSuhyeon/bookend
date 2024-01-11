package com.bookend.security.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String username;
    private String password;
}
