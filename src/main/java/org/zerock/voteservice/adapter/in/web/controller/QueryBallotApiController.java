package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.controller.docs.ballot.QueryBallotApiDoc;
import org.zerock.voteservice.adapter.in.web.processor.BallotQueryProcessorResult;
import org.zerock.voteservice.adapter.in.web.processor.BallotQueryProcessor;

import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.QueryBallotRequestDto;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

@Log4j2
@RestController
public class QueryBallotApiController extends QueryApiEndpointMapper {

    private final BallotQueryProcessor ballotQueryProcessor;

    public QueryBallotApiController(BallotQueryProcessor ballotQueryProcessor) {
        this.ballotQueryProcessor = ballotQueryProcessor;
    }

    @QueryBallotApiDoc
    @GetMapping("/{userHash}/votes")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @PathVariable(value = "userHash") final String userHash
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer currentUid = userDetails.getUid();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating getUserVotes API call: [Path: /{}/votes, Method: GET]", logPrefix, userHash);
        log.debug("{}Authenticated User Info: [Username:{}, Role: {}]", logPrefix, userDetails.getUsername(), role);
        log.debug("{}Received Path Variable: [Userhash: {}]", logPrefix, userHash);

        try {
            BallotQueryProcessorResult hashValidationResult = this.ballotQueryProcessor.validateUserHash(userHash);

            if (!hashValidationResult.getSuccess()) {
                log.warn("{}User hash validation failed: [QueryUserHash: {}, Status: {}, Message: {}]",
                        logPrefix, userHash, hashValidationResult.getStatus(), hashValidationResult.getMessage());

                return this.ballotQueryProcessor.getErrorResponse(hashValidationResult);
            }

            QueryBallotRequestDto dto = QueryBallotRequestDto.builder()
                    .userHash(userHash)
                    .build();

            BallotQueryProcessorResult result = this.ballotQueryProcessor.processBallotQuery(dto);

            if (!result.getSuccess()) {
                log.warn("{}Vote query processing failed: [QueryUserHash: {}, Status: {}, Message: {}]",
                        logPrefix, userHash, result.getStatus(), result.getMessage());
                return this.ballotQueryProcessor.getErrorResponse(result);
            }

            log.info("{}Vote query successful: [QueryUserHash: {}, Status: {}, Found {} votes]",
                    logPrefix, userHash, result.getStatus(), result.getBallotList() != null ? result.getBallotList().size() : 0);

            return this.ballotQueryProcessor.getSuccessResponse(dto, result);
        } catch (GrpcServiceUnavailableException e) {
            log.error("{}{}", logPrefix, e.getMessage());
            return this.ballotQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (io.grpc.StatusRuntimeException e) {
            return GrpcExceptionHandler.handleGrpcStatusRuntimeExceptionInController(
                    currentUid, e, this.ballotQueryProcessor
            );
        } catch (Exception e) {
            log.error("{}An unexpected error occurred during vote query for userHash: {}. Error: {}", logPrefix, userHash, e.getMessage(), e);
            return this.ballotQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
