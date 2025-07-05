package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.UserCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.UserValidationRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.UserCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.UserValidationProcessor;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterService;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterServiceResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserValidationResponseResult;
import org.zerock.voteservice.tool.time.DateConverter;

@Log4j2
@RestController
public class UserRegisterApiController extends UserApiEndpointMapper {

    private final UserValidationProcessor userValidationProcessor;
    private final UserCachingProcessor userCachingProcessor;
    private final UserRegisterService userRegisterService;

    public UserRegisterApiController(
            UserValidationProcessor userValidationProcessor,
            UserCachingProcessor userCachingProcessor,
            UserRegisterService userRegisterService
    ) {
        this.userValidationProcessor = userValidationProcessor;
        this.userCachingProcessor = userCachingProcessor;
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<? extends ResponseDto> register(
            @RequestBody UserRegisterRequestDto dto
    ) {
        String logPrefix = String.format("[AttemptingRegisterUsername:%s] ", dto.getUsername());

        log.debug("{}>>>>>> Initiating user registration API call: [Path: /register, Method: POST]", logPrefix);

        UserRegisterServiceResult validationServiceResult = this.userRegisterService.validateUserExistence(
                dto.getUsername(), dto.getEmail(), dto.getPhoneNumber()
        );

        if (!validationServiceResult.getSuccess()) {
            log.debug("{}User validation failed for registration: [Status: {}]",
                    logPrefix, validationServiceResult.getStatus().getCode());

            return this.userRegisterService.getFailureResponseEntity(validationServiceResult);
        }

        log.debug("{}User L2 validation successful", logPrefix);

        UserRegisterServiceResult userSavedServiceResult = userRegisterService.register(dto);

        if (!userSavedServiceResult.getSuccess()) {
            log.warn("{}User save to DB failed during registration: [Status: {}, Message: {}]",
                    logPrefix, userSavedServiceResult.getStatus().getCode(), userSavedServiceResult.getStatus().getMessage());

            return this.userRegisterService.getFailureResponseEntity(userSavedServiceResult);
        }

        Integer savedUserUid = userSavedServiceResult.getUid();
        String savedUsername = userSavedServiceResult.getUsername();

        log.debug("{}User successfully saved to DB: [UID: {}]", logPrefix, savedUserUid);

        UserValidationRequestDto validationRequestDto = UserValidationRequestDto.builder()
                .uid(savedUserUid)
                .userHash(userSavedServiceResult.getUserHash())
                .build();

        GrpcUserValidationResponseResult validationProcessorResult = this.userValidationProcessor.execute(validationRequestDto);

        if (!validationProcessorResult.getSuccess()) {
            this.userRegisterService.rollbackUserCreation(savedUsername, savedUserUid);

            return this.userValidationProcessor.getFailureResponseEntity(validationProcessorResult);
        }

        log.debug("{}User L3 validation successful", logPrefix);

        UserCachingRequestDto CachingRequestDto = UserCachingRequestDto.builder()
                .uid(savedUserUid)
                .userHash(userSavedServiceResult.getUserHash())
                .gender(userSavedServiceResult.getUserEntity().getGender())
                .birthDate(DateConverter.toTimestamp(userSavedServiceResult.getUserEntity().getBirthDate()))
                .build();

        GrpcUserCachingResponseResult cachingProcessorResult = this.userCachingProcessor.execute(CachingRequestDto);

        if (!cachingProcessorResult.getSuccess()) {
            this.userRegisterService.rollbackUserCreation(savedUsername, savedUserUid);

            return this.userCachingProcessor.getFailureResponseEntity(cachingProcessorResult);
        }

        log.debug("{}User successfully cached to L3-MongoDB: [UserHash: {}]",
                logPrefix, userSavedServiceResult.getUserHash());

        return this.userCachingProcessor.getSuccessResponseEntity(CachingRequestDto, cachingProcessorResult);
    }
}
