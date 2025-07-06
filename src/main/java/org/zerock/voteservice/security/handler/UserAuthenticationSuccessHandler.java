package org.zerock.voteservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserAuthenticationSuccessResponseDto;
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
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();
        String logPrefix = String.format("[Username:%s] ", userDetails.getUsername());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=utf-8");

        String token = this.getJwtAccessToken(userDetails, authentication);
        UserAuthenticationSuccessResponseDto successDto = this.getSuccessResponseDto(userDetails);

        response.addHeader("Authorization", "Bearer " + token);

        log.debug("{}JWT token generated and added to header for user", logPrefix);

        response.getWriter().write(objectMapper.writeValueAsString(successDto));
        response.getWriter().flush();

        log.debug("{}Authentication success response sent to client for user: [Status: {}, Message: {}]",
                logPrefix, successDto.getStatus(), successDto.getMessage());

        String maskedToken = "..." + token.substring(token.length() - 10);
        log.info("{}Authentication successed. JWT issued: [Token: \"{}\"]", logPrefix, maskedToken);
    }

    private String getJwtAccessToken(UserAuthenticationDetails userDetails, Authentication authentication) {
        Integer uid = userDetails.getUid();
        String username = userDetails.getUsername();
        String userHash = userDetails.getUserHash();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("사용자에게 할당된 권한이 없습니다."));

        return jwtUtil.createJwt(uid, userHash, username, role);
    }

    private UserAuthenticationSuccessResponseDto getSuccessResponseDto(UserAuthenticationDetails userDetails) {
        return UserAuthenticationSuccessResponseDto.builder()
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
