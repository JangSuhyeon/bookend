package com.bookend.security.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/login")
public class LoginController {

    // 로그인(메인) 화면으로 이동
    @GetMapping("/page")
    public String goToLogin() {
        return "/login/login";
    }

}
