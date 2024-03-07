package com.fbd.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class UserUtils {
    public static Optional<String> getCurrentUserLogin() {
        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();
        return Objects.isNull(authentication) || authentication.getName().isEmpty()
                ? Optional.empty()
                : Optional.of(authentication.getName());
    }
}
