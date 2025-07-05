package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.controller.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.BallotListQueryRequestDto;

import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.processor.BallotListQueryProcessor;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@RestController
public class BallotQueryApiController extends QueryApiEndpointMapper {

    private final ControllerHelper controllerHelper;
    private final BallotListQueryProcessor ballotListQueryProcessor;

    public BallotQueryApiController(
            ControllerHelper controllerHelper,
            BallotListQueryProcessor ballotListQueryProcessor
    ) {
        this.controllerHelper = controllerHelper;
        this.ballotListQueryProcessor = ballotListQueryProcessor;
    }

    @GetMapping("/{userHash}/votes")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @PathVariable(value = "userHash") final String userHash
    ) {
        UserAuthenticationDetails userDetails = this.controllerHelper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating getUserVotes API call: [Path: /{}/votes, Method: GET]", logPrefix, userHash);
        log.debug("{}Authenticated User Info: [Username: {}, Role: {}]", logPrefix, userDetails.getUsername(), role);
        log.debug("{}Received Path Variable: [Userhash: {}]", logPrefix, userHash);

        BallotListQueryRequestDto requestDto = BallotListQueryRequestDto.builder()
                .userHash(userHash)
                .build();

        GrpcBallotListQueryResponseResult result = this.ballotListQueryProcessor.execute(requestDto);

        return result.getSuccess()
                ? this.ballotListQueryProcessor.getSuccessResponseEntity(requestDto, result)
                : this.ballotListQueryProcessor.getFailureResponseEntity(result);
    }
}
