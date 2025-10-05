package org.zerock.voteservice.adapter.in.web.controller.helper;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class ControllerHelper {
    public UserAuthenticationDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserAuthenticationDetails) authentication.getPrincipal();
    }

    public ResponseEntity<? extends ResponseDto> failureHook(
            Throwable e,
            String logPrefix,
            HttpStatus httpStatus,
            String status,
            String message
    ) {
        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .message(message)
                .status(status)
                .httpStatusCode(httpStatus.value())
                .build();

        if (e != null) {
            log.error("{}API Call Failed. Status: {}, Message: {} [Detail: {}]", logPrefix, httpStatus.value(), message, status, e);
        } else {
            log.warn("{}API Call Failed. Status: {}, Message: {} [Detail: {}]", logPrefix, httpStatus.value(), message, status);
        }

        return new ResponseEntity<>(failureDto, httpStatus);
    }

    public ResponseEntity<? extends ResponseDto> failureHook(
            String logPrefix,
            HttpStatus httpStatus,
            String status,
            String message
    ) {
        return failureHook(null, logPrefix, httpStatus, status, message);
    }
}
