package com.bookend.security.join.service;

import com.bookend.security.domain.Role;
import com.bookend.security.domain.dto.SessionUser;
import com.bookend.security.domain.dto.UserResponseDto;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    // 게스트 회원가입
    public UserResponseDto registerGuestUser() {

        long currentTimeMillis = System.currentTimeMillis(); // 게스트 이름 뒤에 붙힐 현재 시간
        String encryptedPassword = passwordEncoder.encode("test010"); // 게스트 비밀번호

        // User 객체 생성
        User user = User.builder()
                .username("GUEST" + currentTimeMillis)
                .password(encryptedPassword)
                .name("게스트" + currentTimeMillis)
                .role(Role.GUEST)
                .build();

        userRepository.save(user); // User 저장
        httpSession.invalidate(); // 현재 세션을 무효화 시킴
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장

        return UserResponseDto.toDto(user); // Entity -> DTO
    }
}
