package org.zerock.voteservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserRefreshTokenSuccessResponseDto;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.tool.http.HttpHelper;

import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class JwtRefreshAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final PublicEndpointsProperties publicEndpointsProperties;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        HttpHelper httpHelper = new HttpHelper(request, response);

        String accessToken = httpHelper.getAccessToken();
        String refreshToken = httpHelper.getRefreshToken();

        if (jwtUtil.isExpiredToken(accessToken)) {
            Claims claims = jwtUtil.extractAllClaims(refreshToken);

            String username = jwtUtil.getUsername(claims);
            String role = jwtUtil.getRole(claims);
            String userHash = jwtUtil.getUserHash(claims);
            Integer uid = jwtUtil.getUid(claims);
            String logPrefix = String.format("[UID:%d] ", uid);

            String newAccessToken = jwtUtil.createAccessJwt(uid, userHash, username, role);
            String newRefreshToken = jwtUtil.createRefreshJwt(uid, userHash, username, role);

            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json; charset=utf-8");

            response.addHeader("Authorization", "Bearer " + newAccessToken);
            log.debug("{}Access-JWT generated and added to header for user", logPrefix);

            response.addCookie(HttpHelper.createRefreshTokenCookie(newRefreshToken, jwtUtil.getRefreshJwtExpireSeconds()));
            log.debug("{}Refresh-JWT generated and added to cookie for user", logPrefix);

            UserRefreshTokenSuccessResponseDto successDto = UserRefreshTokenSuccessResponseDto.builder()
                    .success(true)
                    .status("REFRESHED_TOKEN")
                    .httpStatusCode(HttpStatus.OK.value())
                    .message("토큰이 성공적으로 재발급 되었습니다. 새로운 액세스 토큰으로 다시 시도해주세요.")
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(successDto));
            response.getWriter().flush();
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludedPaths = publicEndpointsProperties.getPermittedEndpoints();
        String requestUri = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (String pattern : excludedPaths) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }

        return false;
    }
}
