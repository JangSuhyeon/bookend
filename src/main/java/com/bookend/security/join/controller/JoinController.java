package com.bookend.security.join.controller;

import com.bookend.security.join.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/join")
public class JoinController {

    private final JoinService joinService;

    // 게스트 계정 생성
    @GetMapping("/guest")
    public ResponseEntity<String> registerGuestUser() {

        String username = joinService.registerGuestUser();

        return ResponseEntity.ok(username);
    }

}
