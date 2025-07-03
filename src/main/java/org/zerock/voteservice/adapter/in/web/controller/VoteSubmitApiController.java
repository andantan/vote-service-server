package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.controller.docs.submit.VoteSubmitApiDoc;
import org.zerock.voteservice.adapter.in.web.processor.BallotCreateProcessorResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.dto.VoteSubmitBallotDto;
import org.zerock.voteservice.adapter.in.web.dto.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.BallotCreateProcessor;
import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

@Log4j2
@RestController
public class VoteSubmitApiController extends VoteApiEndpointMapper {
    private final BallotCreateProcessor ballotCreateProcessor;

    public VoteSubmitApiController(BallotCreateProcessor ballotCreateProcessor) {
        this.ballotCreateProcessor = ballotCreateProcessor;
    }

    @VoteSubmitApiDoc
    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> submitVote(
            @RequestBody VoteSubmitRequestDto dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer currentUid = userDetails.getUid();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating submitVote API call: [Path: /submit, Method: POST]", logPrefix);
        log.debug("{}Authenticated User Info: [Username: {}, Roles: {}]", logPrefix, userDetails.getUsername(), roles);
        log.debug("{}Received Request DTO: [Topic: {}, Option: {}]", logPrefix, dto.getTopic(), dto.getOption());

        VoteSubmitBallotDto voteSubmitBallotDto = VoteSubmitBallotDto.builder()
                .userHash(userDetails.getUserHash())
                .topic(dto.getTopic())
                .option(dto.getOption())
                .build();

        try {
            BallotCreateProcessorResult result = this.ballotCreateProcessor.processBallotCreation(voteSubmitBallotDto);

            if (!result.getSuccess()) {
                log.warn("{}Vote submission processing failed: [Topic: {}, Status: {}, Message: {}]",
                        logPrefix, dto.getTopic(), result.getStatus(), result.getMessage());
                return this.ballotCreateProcessor.getErrorResponse(result);
            }

            log.info("{}Vote submission successful: [Topic: {}, Status: {}, Message: {}]",
                    logPrefix, dto.getTopic(), result.getStatus(), result.getMessage());
            return this.ballotCreateProcessor.getSuccessResponse(voteSubmitBallotDto, result);

        } catch (GrpcServiceUnavailableException e) {
            log.error("{}{}", logPrefix, e.getMessage());
            return this.ballotCreateProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (io.grpc.StatusRuntimeException e) {
            return GrpcExceptionHandler.handleGrpcStatusRuntimeExceptionInController(
                    currentUid, e, this.ballotCreateProcessor
            );
        } catch (Exception e) {
            log.error("{}An unexpected error occurred during vote submission for topic: {}, option: {}. Error: {}", logPrefix, dto.getTopic(), dto.getOption(), e.getMessage(), e);

            return this.ballotCreateProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
