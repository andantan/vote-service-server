package org.zerock.voteservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationResponseDto;
import org.zerock.voteservice.security.jwt.JwtUtil;

import java.io.IOException;


@Log4j2
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public UserAuthenticationSuccessHandler(
            ObjectMapper objectMapper, JwtUtil jwtUtil
    ) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=utf-8");

        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        String token = this.getJwtAccessToken(userDetails, authentication);
        UserAuthenticationResponseDto successDto = this.getSuccessResponseDto(userDetails);

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write(objectMapper.writeValueAsString(successDto));
        response.getWriter().flush();
    }

    private String getJwtAccessToken(UserAuthenticationDetails userDetails, Authentication authentication) {
        Integer uid = userDetails.getUid();
        String username = userDetails.getUsername();
        String userHash = userDetails.getUserHash();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("사용자에게 할당된 권한이 없습니다."));

        return jwtUtil.createJwt(uid, username, userHash, role);
    }

    private UserAuthenticationResponseDto getSuccessResponseDto(UserAuthenticationDetails userDetails) {
        return UserAuthenticationResponseDto.builder()
                .success(true)
                .message("로그인에 성공하였습니다.")
                .status("OK")
                .httpStatusCode(HttpStatus.OK.value())
                .uid(userDetails.getUid())
                .userHash(userDetails.getUserHash())
                .expireMinutes(jwtUtil.getExpireMinutes())
                .build();
    }
}
