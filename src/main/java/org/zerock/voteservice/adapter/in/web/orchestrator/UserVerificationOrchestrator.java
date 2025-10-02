package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserEmailVerificationWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserVerificationRequestDto;
import org.zerock.voteservice.adapter.in.web.service.MailService;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationResult;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationService;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRepository;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.tool.random.StringGenerator;

import java.util.Optional;

@Log4j2
@Component
public class UserVerificationOrchestrator extends AbstractOrchestrator<UserEmailVerificationWebClientRequestDto, ResponseDto> {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserVerificationService userVerificationService;

    @Value("${code.gmail.verification.digest}")
    private String verificationDigest;

    @Value("${code.gmail.verification.length}")
    private int verificationLength;

    protected UserVerificationOrchestrator(
            ControllerHelper controllerHelper,
            MailService mailService,
            UserRepository userRepository,
            UserVerificationService userVerificationService
    ) {
        super(controllerHelper);
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.userVerificationService = userVerificationService;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            UserEmailVerificationWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting generate email-verification code for username: {}", logPrefix, requestDto.getEmail());

        UserVerificationRequestDto dto = UserVerificationRequestDto.builder()
                .username(requestDto.getUsername())
                .realname(requestDto.getRealName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .category(requestDto.getCategory())
                .build();

        if (requestDto.getCategory().equals("reset-password")) {
            Optional<UserEntity> optionalUser = this.userRepository.findByUsername(requestDto.getUsername());

            if (optionalUser.isEmpty()) {
                return this.userVerificationService.getNotFoundUserResponseEntity();
            }

            UserEntity userEntity = optionalUser.get();

            if (!userEntity.getEmail().equals(requestDto.getEmail())
            || !userEntity.getPhoneNumber().equals(requestDto.getPhoneNumber())
            || !userEntity.getRealName().equals(requestDto.getRealName())) {
                return this.userVerificationService.getNotFoundUserResponseEntity();
            }

            dto.setUid(userEntity.getUid());
        }

        String verificationCode = StringGenerator.generateRandomString(this.verificationDigest, this.verificationLength);

        try {
            this.mailService.sendVerificationCode(requestDto.getEmail(), verificationCode);
        } catch (Exception e) {
            log.error("{}Failed to send verification-code for email: {}", logPrefix, requestDto.getEmail(), e);
            return this.getEmailFailureResponse();
        }

        dto.setCode(verificationCode);

        UserVerificationResult result = this.userVerificationService.cacheVerificationCode(dto);

        if (!result.getSuccess()) {
            log.warn("{}User save to DB failed during verification: [Status: {}, Message: {}]",
                    logPrefix, result.getStatus().getCode(), result.getStatus().getMessage());
            return this.userVerificationService.getFailureResponseEntity(result);
        }

        return this.userVerificationService.getSuccessResponseEntity(result);
    }

    private ResponseEntity<? extends ResponseDto> getEmailFailureResponse() {
        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status("EMAIL_SERVICE_ERROR")
                .message("이메일 전송에 실패하였습니다.")
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));

    }
}
