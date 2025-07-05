package org.zerock.voteservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.CommonFailureResponseDto;

import java.io.IOException;

@Log4j2
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public UserAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        log.warn("Access denied for authenticated user. Path: {}, Exception: {}",
                request.getRequestURI(), accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        CommonFailureResponseDto errorResponseDto = this.getErrorResponseDto();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
        response.getWriter().flush();
    }

    private CommonFailureResponseDto getErrorResponseDto() {
        return CommonFailureResponseDto.builder()
                .success(false)
                .message("접근 권한이 없습니다.")
                .status("ACCESS_DENIED")
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
    }
}
