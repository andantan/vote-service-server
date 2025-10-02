package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.UserCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.UserValidationGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserRegisterWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserVerificationRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.UserCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.UserValidationProcessor;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterService;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterServiceResult;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationResult;
import org.zerock.voteservice.adapter.in.web.service.UserVerificationService;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserValidationResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.tool.time.DateConverter;

@Log4j2
@Component
public class UserCreateOrchestrator extends AbstractOrchestrator<UserRegisterWebClientRequestDto, ResponseDto> {

    private final UserValidationProcessor userValidationProcessor;
    private final UserCachingProcessor userCachingProcessor;
    private final UserRegisterService userRegisterService;
    private final UserVerificationService userVerificationService;

    public UserCreateOrchestrator(
            ControllerHelper controllerHelper,
            UserValidationProcessor userValidationProcessor,
            UserCachingProcessor userCachingProcessor,
            UserRegisterService userRegisterService,
            UserVerificationService userVerificationService
    ) {
        super(controllerHelper);
        this.userValidationProcessor = userValidationProcessor;
        this.userCachingProcessor = userCachingProcessor;
        this.userRegisterService = userRegisterService;
        this.userVerificationService = userVerificationService;
    }


    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            UserRegisterWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting create user for username: {}", logPrefix, requestDto.getUsername());

        UserVerificationRequestDto verificationRequestDto = UserVerificationRequestDto.builder()
                .username(requestDto.getUsername())
                .realname(requestDto.getRealName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .code(requestDto.getVerificationCode())
                .category("register")
                .build();

        UserVerificationResult verificationServiceResult = this.userVerificationService.matchVerificationCode(verificationRequestDto);

        if (!verificationServiceResult.getSuccess()) {
            log.debug("{}User email-verification failed for registration: [Status: {}]",
                    logPrefix, verificationServiceResult.getStatus().getCode());

            return this.userVerificationService.getFailureResponseEntity(verificationServiceResult);
        }

        UserRegisterRequestDto registerRequestDto = UserRegisterRequestDto.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .realName(requestDto.getRealName())
                .srnPart(requestDto.getSrnPart())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        UserRegisterServiceResult validationServiceResult = this.userRegisterService.validateUserExistence(registerRequestDto);

        if (!validationServiceResult.getSuccess()) {
            log.debug("{}User validation failed for registration: [Status: {}]",
                    logPrefix, validationServiceResult.getStatus().getCode());

            return this.userRegisterService.getFailureResponseEntity(validationServiceResult);
        }

        UserRegisterServiceResult userSavedServiceResult = userRegisterService.register(registerRequestDto);

        if (!userSavedServiceResult.getSuccess()) {
            log.warn("{}User save to DB failed during registration: [Status: {}, Message: {}]",
                    logPrefix, userSavedServiceResult.getStatus().getCode(), userSavedServiceResult.getStatus().getMessage());

            return this.userRegisterService.getFailureResponseEntity(userSavedServiceResult);
        }

        Long savedUserUid = userSavedServiceResult.getUid();
        String savedUsername = userSavedServiceResult.getUsername();

        log.debug("{}User successfully saved to DB: [UID: {}]", logPrefix, savedUserUid);

        UserValidationGrpcRequestDto validationRequestDto = UserValidationGrpcRequestDto.builder()
                .uid(savedUserUid)
                .userHash(userSavedServiceResult.getUserHash())
                .build();

        GrpcUserValidationResponseResult validationProcessorResult = processStep(
                userValidationProcessor, validationRequestDto, logPrefix, "User L3 Validation"
        );

        if (!validationProcessorResult.getSuccess()) {
            this.userRegisterService.rollbackUserCreation(savedUsername, savedUserUid);

            return createFailureResponse(
                    userValidationProcessor, validationProcessorResult, logPrefix, "User L3 Validation"
            );
        }

        UserCachingGrpcRequestDto cachingRequestDto = UserCachingGrpcRequestDto.builder()
                .uid(savedUserUid)
                .userHash(userSavedServiceResult.getUserHash())
                .gender(userSavedServiceResult.getUserEntity().getGender())
                .birthDate(DateConverter.toTimestamp(userSavedServiceResult.getUserEntity().getBirthDate()))
                .build();

        GrpcUserCachingResponseResult cachingProcessorResult = processStep(
                userCachingProcessor, cachingRequestDto, logPrefix, "User Caching"
        );

        if (!cachingProcessorResult.getSuccess()) {
            this.userRegisterService.rollbackUserCreation(savedUsername, savedUserUid);

            return createFailureResponse(
                    userCachingProcessor, cachingProcessorResult, logPrefix, "User Caching"
            );
        }

        log.debug("{}User successfully cached to L3-MongoDB: [UserHash: {}]",
                logPrefix, userSavedServiceResult.getUserHash());

        this.userVerificationService.deleteVerificationCode(verificationRequestDto);

        return createSuccessResponse(
                userCachingProcessor, cachingRequestDto, cachingProcessorResult, logPrefix, "User Create"
        );
    }
}
