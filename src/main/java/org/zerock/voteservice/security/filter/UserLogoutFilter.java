package org.zerock.voteservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserLogoutSuccessResponseDto;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.security.user.UserRefreshTokenRotateService;
import org.zerock.voteservice.tool.http.HttpHelper;

import java.io.IOException;

@Log4j2
public class UserLogoutFilter extends GenericFilterBean {
    private final UserRefreshTokenRotateService userRefreshTokenRotateService;
    private final PublicEndpointsProperties publicEndpointsProperties;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public UserLogoutFilter(
            UserRefreshTokenRotateService userRefreshTokenRotateService,
            PublicEndpointsProperties publicEndpointsProperties,
            ObjectMapper objectMapper,
            JwtUtil jwtUtil
    ) {
        this.userRefreshTokenRotateService = userRefreshTokenRotateService;
        this.publicEndpointsProperties = publicEndpointsProperties;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
    ) throws IOException, ServletException {
        attemptLogout((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void attemptLogout(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        if (!request.getRequestURI().matches(publicEndpointsProperties.getLogoutEndpoint())) {
            return;
        }

        if (!request.getMethod().equals("POST")) {
            return;
        }

        HttpHelper httpHelper = new HttpHelper(request, response);
        Claims claims = jwtUtil.extractAllClaims(httpHelper.getAccessToken());
        Long uid = jwtUtil.getUid(claims);
        String logPrefix = String.format("[UID:%d] ", uid);

        log.debug("{}Attempting logout service - trying to remove refresh token", logPrefix);

        this.userRefreshTokenRotateService.removeRefreshToken(httpHelper.getRefreshToken());
        log.debug("{}Refresh rotate removed to db for user", logPrefix);

        UserLogoutSuccessResponseDto successDto = this.getSuccessResponseDto(uid);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=utf-8");
        response.addCookie(HttpHelper.createRefreshTokenCookie(null, 0));
        log.debug("{}Refresh-JWT 0-expiration to cookie for user", logPrefix);

        response.getWriter().write(objectMapper.writeValueAsString(successDto));
        response.getWriter().flush();

        log.debug("{}Logout successed and response sent to client for user: [Status: {}, Message: {}]",
                logPrefix, successDto.getStatus(), successDto.getMessage());
    }

    private UserLogoutSuccessResponseDto getSuccessResponseDto(Long uid) {
        return UserLogoutSuccessResponseDto.builder()
                .success(true)
                .message("로그아웃에 성공하였습니다.")
                .status("OK")
                .httpStatusCode(HttpStatus.OK.value())
                .uid(uid)
                .build();
    }
}
