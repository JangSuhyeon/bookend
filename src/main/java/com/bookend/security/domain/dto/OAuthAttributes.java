package com.bookend.security.domain.dto;

import com.bookend.security.domain.Role;
import com.bookend.security.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String username;
    private String picture;

    public static OAuthAttributes of(String registrationId,
                                     String usernameAttributeName,
                                     Map<String, Object> attributes) {

        return ofGoogle(usernameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .username((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .username(username)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
