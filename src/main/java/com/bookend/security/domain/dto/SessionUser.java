package com.bookend.security.domain.dto;

import com.bookend.security.domain.entity.User;

public class SessionUser {

    private Long id;
    private String name;
    private String username;
    private String picture;

    public SessionUser(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.picture = user.getPicture();
    }
}
