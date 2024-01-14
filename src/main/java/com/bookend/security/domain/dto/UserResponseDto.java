package com.bookend.security.domain.dto;

import com.bookend.security.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String username;    // 로그인 시 이용되는 아이디
    private String password;    // 로그인 시 이용되는 비밀번호

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
