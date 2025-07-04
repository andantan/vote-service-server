package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.controller.docs.register.UserRegisterApiDoc;
import org.zerock.voteservice.adapter.in.web.processor.UserRegisterProcessor;
import org.zerock.voteservice.adapter.in.web.processor.UserRegisterProcessorResult;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterService;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterServiceResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

@Log4j2
@RestController
public class UserApiRegisterController extends UserApiEndpointMapper {

    private final UserRegisterService userRegisterService;
    private final UserRegisterProcessor userRegisterProcessor;

    public UserApiRegisterController(
            UserRegisterService userRegisterService,
            UserRegisterProcessor userRegisterProcessor
    ) {
        this.userRegisterService = userRegisterService;
        this.userRegisterProcessor = userRegisterProcessor;
    }

    @UserRegisterApiDoc
    @PostMapping("/register")
    public ResponseEntity<? extends ResponseDto> register(
            @RequestBody UserRegisterRequestDto dto
    ) {
        String logPrefix = String.format("[AttemptingRegisterUsername:%s] ", dto.getUsername());
        Integer currentUid = null;

        log.debug("{}>>>>>> Initiating user registration API call: [Path: /register, Method: POST]", logPrefix);

        UserRegisterServiceResult userSavedResult;

        try {
            UserRegisterServiceResult userValidationResult = this.userRegisterService.validateUserExistence(
                    dto.getUsername(), dto.getEmail()
            );

            if (!userValidationResult.getSuccess()) {
                log.debug("{}User validation failed for registration: [Status: {}]",
                        logPrefix, userValidationResult.getStatus());

                return this.userRegisterService.getErrorResponse(userValidationResult);
            }

            log.debug("{}User validation successful", logPrefix);

            userSavedResult = userRegisterService.register(dto);

            if (!userSavedResult.getSuccess()) {
                log.warn("{}User save to DB failed during registration: [Status: {}, Message: {}]",
                        logPrefix, userSavedResult.getStatus(), userSavedResult.getMessage());

                return this.userRegisterService.getErrorResponse(userSavedResult);
            }

            currentUid = userSavedResult.getUid();

            log.debug("{}User successfully saved to DB: [UID: {}]", logPrefix, userSavedResult.getUid());

            UserRegisterProcessorResult userCachedResult = this.userRegisterProcessor.processUserCreation(userSavedResult);

            if (!userCachedResult.getSuccess()) {
                this.userRegisterService.rollbackUserCreation(dto.getUsername(), userSavedResult.getUid());

                return this.userRegisterProcessor.getErrorResponse(userCachedResult);
            }

            log.info("{}User successfully registered: [UID: {}]", logPrefix, userSavedResult.getUid());

            return this.userRegisterProcessor.getSuccessDto(dto, userCachedResult);
        } catch (GrpcServiceUnavailableException e) {
            log.error("{}{}", logPrefix, e.getMessage());

            this.userRegisterService.rollbackUserCreation(dto.getUsername(), currentUid);

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (io.grpc.StatusRuntimeException e) {
            ResponseEntity<? extends ResponseDto> errorEntity = GrpcExceptionHandler.handleGrpcStatusRuntimeExceptionInController(
                    currentUid, e, this.userRegisterProcessor
            );

            this.userRegisterService.rollbackUserCreation(dto.getUsername(), currentUid);

            return errorEntity;

        } catch (Exception e) {
            log.error("{}An unexpected error occurred during user registration for user. Attempting to rollback MariaDB entry.", dto.getUsername());

            if (currentUid != null) {
                this.userRegisterService.rollbackUserCreation(dto.getUsername(), currentUid);
            } else {
                log.warn("{}No UID available for MariaDB rollback after unexpected error during user registration for user", logPrefix);
            }

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
