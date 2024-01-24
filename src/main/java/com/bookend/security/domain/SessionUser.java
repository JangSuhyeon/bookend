package com.bookend.security.domain;

import com.bookend.security.domain.entity.User;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SessionUser {

    private Long userId;
    private String name;
    private String username;
    private String picture;
    private String role;

    public SessionUser(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.picture = user.getPicture();
        this.role = user.getRole().getKey();
    }
}
