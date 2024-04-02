package com.bookend.security.service;

import com.bookend.security.domain.Role;
import com.bookend.security.domain.SessionUser;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.info("oAuth2User : {}", oAuth2User.toString()); // 사용자 정보

        String usernameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("usernameAttributeName : {}", usernameAttributeName); // 소셜서비스 id : sub(Google)

        User user = saveOrUpdate(oAuth2User.getAttributes());
        httpSession.invalidate();  // 세션 초기화
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보 저장(저장하지 않으면 @LoginUser를 사용할 수 없다.)

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                        oAuth2User.getAttributes(),
                        usernameAttributeName);
    }

    // 회원 정보 저장 및 업데이트
    private User saveOrUpdate(Map<String, Object> attributes) {
        User user = userRepository.findByUsername((String) attributes.get("email"))
                .map(entity -> entity.update((String) attributes.get("name"), (String) attributes.get("picture")))
                .orElse(User.builder()
                        .username((String) attributes.get("email"))
                        .name((String) attributes.get("name"))
                        .role(Role.USER) // 게스트 권한 부여
                        .picture((String) attributes.get("picture"))
                        .build());

        return userRepository.save(user);
    }
}
