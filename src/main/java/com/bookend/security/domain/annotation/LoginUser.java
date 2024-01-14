package com.bookend.security.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메소드 파라미터에 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임 중에 사용
public @interface LoginUser {
}
