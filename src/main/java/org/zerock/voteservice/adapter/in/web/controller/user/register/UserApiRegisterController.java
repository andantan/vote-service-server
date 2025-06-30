package org.zerock.voteservice.adapter.in.web.controller.user.register;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.user.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.user.register.docs.UserRegisterApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.user.register.processor.UserRegisterProcessor;
import org.zerock.voteservice.adapter.in.web.controller.user.register.processor.UserRegisterProcessorResult;
import org.zerock.voteservice.adapter.in.web.controller.user.register.service.UserRegisterService;
import org.zerock.voteservice.adapter.in.web.controller.user.register.service.UserRegisterServiceResult;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;

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
                log.error("{}User creation process failed (caching): [UID: {}, Status: {}, Message: {}]. Attempting MariaDB rollback.",
                        logPrefix, currentUid, userCachedResult.getStatus(), userCachedResult.getMessage());

                this.userRegisterService.rollbackUserCreation(userSavedResult.getUid());

                log.debug("{}MariaDB rollback initiated for user: [UID: {}]", logPrefix, currentUid);

                return this.userRegisterProcessor.getErrorResponse(userCachedResult);
            }

            log.info("{}User successfully registered: [UID: {}]", logPrefix, userSavedResult.getUid());

            return this.userRegisterProcessor.getSuccessDto(dto, userCachedResult);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                log.error("{}gRPC Server is unavailable or unreachable for register: [Status: {}]", logPrefix, e.getStatus().getCode());
            } else {
                log.error("{}unexpected gRPC error: Status={}, Description={}", logPrefix, e.getStatus().getCode(), e.getStatus().getDescription());
            }

            log.warn("{}Attempting MariaDB rollback due to gRPC error for user: [UID: {}]", logPrefix, currentUid);
            this.userRegisterService.rollbackUserCreation(currentUid);
            log.info("{}MariaDB rollback completed due to gRPC error for user: [UID: {}]", logPrefix, currentUid);

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        } catch (RuntimeException e) {
            log.error("{}{}", logPrefix, e.getMessage());
            log.warn("{}Attempting MariaDB rollback due to gRPC error for user: [UID: {}]", logPrefix, currentUid);
            this.userRegisterService.rollbackUserCreation(currentUid);
            log.info("{}MariaDB rollback completed due to gRPC error for user: [UID: {}]", logPrefix, currentUid);
            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        } catch (Exception e) {
            log.error("{}An unexpected error occurred during user registration for user. Attempting to rollback MariaDB entry.", dto.getUsername());

            if (currentUid != null) {
                log.warn("{}Attempting MariaDB rollback due to unexpected error for user: [UID: {}]", logPrefix, currentUid);
                this.userRegisterService.rollbackUserCreation(currentUid);
                log.info("{}MariaDB rollback completed due to unexpected error for user: [UID: {}]", logPrefix, currentUid);
            } else {
                log.warn("{}No UID available for MariaDB rollback after unexpected error during user registration for user", logPrefix);
            }

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
