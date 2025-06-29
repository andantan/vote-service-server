package org.zerock.voteservice.adapter.in.web.controller.user.register;

import io.grpc.Status;
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
        UserRegisterServiceResult userValidationResult = this.userRegisterService.validateUserExistence(
                dto.getUsername(), dto.getEmail()
        );

        if (!userValidationResult.getSuccess()) {
            return this.userRegisterService.getErrorResponse(userValidationResult);
        }

        UserRegisterServiceResult userSavedResult = null;

        try {
            userSavedResult = userRegisterService.register(dto);

            if (!userSavedResult.getSuccess()) {
                return this.userRegisterService.getErrorResponse(userSavedResult);
            }

            UserRegisterProcessorResult userCachedResult = this.userRegisterProcessor.processUserCreation(userSavedResult);

            if (!userCachedResult.getSuccess()) {
                log.error("Failed to process user creation in external cache. Attempting to rollback MariaDB entry for user: {}", dto.getUsername());
                this.userRegisterService.rollbackUserCreation(userSavedResult.getUid());

                return this.userRegisterProcessor.getErrorResponse(userCachedResult);
            }

            log.info("User successfully registered: {} -> id:{}", dto.getUsername(), userSavedResult.getUid());

            return this.userRegisterProcessor.getSuccessDto(dto, userCachedResult);
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                log.error("gRPC Server is unavailable or unreachable: {}. Check server status and network.", e.getMessage());
            } else if (e.getStatus().getCode() == Status.Code.DEADLINE_EXCEEDED) {
                log.error("gRPC call timed out: {}. Consider increasing timeout or checking server performance.", e.getMessage());
            } else {
                log.error("Unexpected gRPC error: Status={}, Description={}", e.getStatus().getCode(), e.getStatus().getDescription());
            }

            if (userSavedResult != null && userSavedResult.getSuccess() && userSavedResult.getUid() != null) {
                this.userRegisterService.rollbackUserCreation(userSavedResult.getUid());
            }

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        } catch (Exception e) {
            log.error("An unexpected error occurred during user registration for user: {}. Attempting to rollback MariaDB entry.", dto.getUsername());

            if (userSavedResult != null && userSavedResult.getSuccess() && userSavedResult.getUid() != null) {
                this.userRegisterService.rollbackUserCreation(userSavedResult.getUid());
            }

            return this.userRegisterProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
