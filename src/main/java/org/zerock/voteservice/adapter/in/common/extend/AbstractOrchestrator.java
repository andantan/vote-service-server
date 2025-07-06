package org.zerock.voteservice.adapter.in.common.extend;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.zerock.voteservice.adapter.in.common.Orchestrator;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

import java.util.stream.Collectors;

@Log4j2
public abstract class AbstractOrchestrator<In extends RestApiRequestDto, Out extends ResponseDto>
        implements Orchestrator<In, Out> {

    protected final ControllerHelper controllerHelper;

    protected AbstractOrchestrator(ControllerHelper controllerHelper) {
        this.controllerHelper = controllerHelper;

        log.info("[Orchestrator] {} initialized for controller", this.getClass().getSimpleName());
    }

    @Override
    public ResponseEntity<? extends Out> orchestrate(In requestDto) {
        UserAuthenticationDetails userDetails = null;
        String logPrefix;

        try {
            userDetails = controllerHelper.getUserDetails();
            logPrefix = String.format("[UID:%d] ", userDetails.getUid());

        } catch (Exception e) {
            if (requestDto instanceof UnauthenticatedRequestDto unauthenticatedDto) {
                logPrefix = String.format("[%s] ", unauthenticatedDto.identifier());

            } else {
                // DTO class name
                log.warn("Failed to get user details or specific identifier for log prefix.", e);

                logPrefix = String.format("[%s] ", requestDto.getClass().getSimpleName());
            }
        }

        log.debug("{}>>>>>> Initiating API Call Orchestration", logPrefix);

        if (userDetails != null) {
            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            log.debug("{}Authenticated User Info: [Username: {}, Roles: {}]", logPrefix, userDetails.getUsername(), roles);
        }

        log.debug("{}Received Request DTO: {}", logPrefix, requestDto.getClass().getSimpleName());

        // Orchestrator core logic
        ResponseEntity<? extends Out> response = executeBusinessLogic(requestDto, userDetails, logPrefix);

        log.debug("{}API Call Orchestration Completed. Status: {}", logPrefix, response.getStatusCode());

        return response;
    }

    protected abstract ResponseEntity<? extends Out> executeBusinessLogic(
            In requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    );

    protected <PIn extends GrpcRequestDto, POut extends GrpcResponseResult> POut processStep(
            Processor<PIn, POut> processor,
            PIn processorRequestDto,
            String logPrefix,
            String stepName
    ) {
        log.debug("{}Starting {} Process.", logPrefix, stepName);

        POut result = processor.execute(processorRequestDto);

        if (result.getSuccess()) {
            log.debug("{} {} PASSED.", logPrefix, stepName);
        } else {
            log.warn("{} {} FAILED. Reason: {}", logPrefix, stepName, result.getMessage());
        }

        return result;
    }

    // Cause: Out extends RequestDto
    @SuppressWarnings("unchecked")
    protected <PIn extends GrpcRequestDto, POut extends GrpcResponseResult> ResponseEntity<? extends Out> createSuccessResponse(
            Processor<PIn, POut> processor,
            PIn processorRequestDto,
            POut processorResult,
            String logPrefix,
            String apiName
    ) {
        log.info("{}{} SUCCESS.", logPrefix, apiName);

        return (ResponseEntity<? extends Out>) processor.getSuccessResponseEntity(processorRequestDto, processorResult);
    }

    // Cause: Out extends RequestDto
    @SuppressWarnings("unchecked")
    protected <PIn extends GrpcRequestDto, POut extends GrpcResponseResult> ResponseEntity<? extends Out> createFailureResponse(
            Processor<PIn, POut> processor,
            POut processorResult,
            String logPrefix,
            String apiName
    ) {
        log.info("{}{} FAILED.", logPrefix, apiName);

        return (ResponseEntity<? extends Out>) processor.getFailureResponseEntity(processorResult);
    }
}
