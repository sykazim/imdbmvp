package com.smk.imdb.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    public static String getEmailOfLoggedInUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
