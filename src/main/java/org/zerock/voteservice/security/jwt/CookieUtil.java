package org.zerock.voteservice.security.jwt;

import jakarta.servlet.http.Cookie;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CookieUtil {
    public Cookie createHttpOnlyCookie(String key, String value, Integer maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
//        cookie.setSecure(true);
        return cookie;
    }
}
