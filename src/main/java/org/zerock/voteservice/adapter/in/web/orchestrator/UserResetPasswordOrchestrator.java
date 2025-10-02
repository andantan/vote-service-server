package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserPasswordResetWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserPasswordModifyRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserVerificationRequestDto;
import org.zerock.voteservice.adapter.in.web.service.UserModifyService;
import org.zerock.voteservice.adapter.in.web.service.UserModifyServiceResult;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationResult;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationService;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class UserResetPasswordOrchestrator extends AbstractOrchestrator<UserPasswordResetWebClientRequestDto, ResponseDto> {

    private final UserModifyService userModifyService;
    private final UserVerificationService userVerificationService;

    protected UserResetPasswordOrchestrator(
            ControllerHelper controllerHelper,
            UserModifyService userModifyService,
            UserVerificationService userVerificationService
    ) {
        super(controllerHelper);
        this.userModifyService = userModifyService;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            UserPasswordResetWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting reset password for username: {}", logPrefix, requestDto.getUsername());

        UserVerificationRequestDto verificationRequestDto = UserVerificationRequestDto.builder()
                .username(requestDto.getUsername())
                .realname(requestDto.getRealName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .code(requestDto.getVerificationCode())
                .category("reset-password")
                .build();

        UserVerificationResult verificationServiceResult = this.userVerificationService.matchVerificationCode(verificationRequestDto);

        if (!verificationServiceResult.getSuccess()) {
            log.debug("{}User email-verification failed while reset-password: [Status: {}]",
                    logPrefix, verificationServiceResult.getStatus().getCode());

            return this.userVerificationService.getFailureResponseEntity(verificationServiceResult);
        }

        UserPasswordModifyRequestDto passwordModifyRequestDto = UserPasswordModifyRequestDto.builder()
                .uid(requestDto.getUid())
                .username(requestDto.getUsername())
                .newPassword(requestDto.getNewPassword())
                .build();

        UserModifyServiceResult result = this.userModifyService.modifyPassword(passwordModifyRequestDto);

        if (!result.getSuccess()) {
            return this.userModifyService.getFailureResponseEntity(result);
        }

        log.debug("{}User successfully modified password info: [UserHash: {}]",
                logPrefix, result.getUserhash());

        this.userVerificationService.deleteVerificationCode(verificationRequestDto);

        return this.userModifyService.getSuccessResponseEntity(result);
    }
}
