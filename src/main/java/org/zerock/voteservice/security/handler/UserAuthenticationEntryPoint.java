package org.zerock.voteservice.security.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Log4j2
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final UserAuthenticationErrorHandler errorHandler;

    public UserAuthenticationEntryPoint(UserAuthenticationErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        HttpStatus httpStatus = statusCode != null ? HttpStatus.valueOf(statusCode) : HttpStatus.FORBIDDEN;
        String message;

        if (errorMessage != null && !errorMessage.isEmpty()) {
            message = errorMessage;
        } else {
            message = authException.getMessage();
        }

        this.errorHandler.handleGenericError(response, httpStatus, message);
    }
}
