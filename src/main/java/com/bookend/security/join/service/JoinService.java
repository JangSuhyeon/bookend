package com.bookend.security.join.service;

import com.bookend.security.domain.Role;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    // 게스트 계정 생성
    public String registerGuestUser() {

        long currentTimeMillis = System.currentTimeMillis(); // 게스트 이름 뒤에 붙힐 현재 시간
        String encryptedPassword = passwordEncoder.encode("test010"); // 게스트 비밀번호 고정 값

        // User 객체 생성
        User user = User.builder()
                .username("GUEST" + currentTimeMillis) // 아이디 중복 방지
                .password(encryptedPassword)
                .name("게스트" + currentTimeMillis)
                .role(Role.GUEST) // 게스트 권한 부여
                .picture("/images/guest_icon.png")
                .build();

        userRepository.save(user); // User 저장
        httpSession.invalidate();  // 세션 초기화
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장(저장하지 않으면 @LoginUser를 사용할 수 없다.)

        return user.getUsername(); // 비밀번호는 고정값이므로 username만 반환
    }
}
