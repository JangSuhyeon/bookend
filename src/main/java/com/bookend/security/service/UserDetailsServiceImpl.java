package com.bookend.security.service;

import com.bookend.security.domain.dto.UserDetailsImpl;
import com.bookend.security.domain.entity.User;
import com.bookend.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username); // 아이디로 회원 조회
        return user.map(UserDetailsImpl::new).orElse(null); // UserDetailsImpl(UserDetails의 구현체)타입으로 변환하여 반환
    }
}
