package org.zerock.voteservice.adapter.in.web.controller.query.ballot;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.query.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.docs.QueryBallotApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.processor.BallotQueryProcessorResult;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.processor.BallotQueryProcessor;

import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.ballot.QueryBallotRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;

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
        String logPrefix = String.format("[UID:%d] ", currentUid);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

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
        } catch (StatusRuntimeException e) {
            Status.Code statusCode = e.getStatus().getCode();
            String statusDescription = e.getStatus().getDescription();

            if (statusCode == Status.Code.UNAVAILABLE) {
                log.error("{}gRPC Server is unavailable or unreachable for vote query. Check server status and network. Error: {}", logPrefix, statusDescription);
            } else if (statusCode == Status.Code.DEADLINE_EXCEEDED) {
                log.error("{}gRPC call timed out for vote query. Consider increasing timeout or checking server performance. Error: {}", logPrefix, statusDescription);
            } else {
                log.error("{}Unexpected gRPC error during vote query: Status={}, Description={}", logPrefix, statusCode, statusDescription, e);
            }

            return this.ballotQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (Exception e) {
            log.error("{}An unexpected error occurred during vote query for userHash: {}. Error: {}", logPrefix, userHash, e.getMessage(), e);
            return this.ballotQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
