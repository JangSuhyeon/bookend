package com.bookend.security.domain.entity;

import com.bookend.chat.domain.entity.ChatMessage;
import com.bookend.chat.domain.entity.ChatUser;
import com.bookend.review.domain.entity.Review;
import com.bookend.security.domain.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;        // PK

    @Column(nullable = false)
    private String name;        // 회원이름

    @Column(nullable = false)
    private String username;    // 아이디(로그인 시 이용)

    @Column
    private String picture;     // 회원사진
    private String password;    // 비밀번호(로그인 시 이용)
    private Date lastConDt;     // 마지막 로그인 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;          // 회원권한

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<>();

    @PrePersist
    protected void setLastConDt() {
        this.lastConDt = new Date(); // User를 저장하기 전 마지막 로그인 시간 등록
    }

    // UserDetailsImple에서 권한목록을 리턴할 때 사용
    public String getRoleKey() {
        return this.role.getKey();
    }

    // OAuth2UserServiceImpl에서 회원 정보 업데이트 시 사용
    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        setLastConDt();
        return this;
    }
}
