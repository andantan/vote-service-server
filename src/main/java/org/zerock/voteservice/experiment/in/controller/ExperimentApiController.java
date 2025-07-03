package org.zerock.voteservice.experiment.in.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryGrpcResult;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentResponseDto;
import org.zerock.voteservice.experiment.in.domain.dto.ExperimentProposalDetailQueryRequestDto;
import org.zerock.voteservice.experiment.in.processor.ExperimentProposalDetailQueryProcessor;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@RestController
public class ExperimentApiController extends ExperimentApiEndpointMapper {

    private final ExperimentProposalDetailQueryProcessor processor;
    private final ExperimentApiControllerHelper helper;

    public ExperimentApiController(ExperimentProposalDetailQueryProcessor processor, ExperimentApiControllerHelper helper) {
        this.processor = processor;
        this.helper = helper;
    }

    @GetMapping("/proposal/{topic}/detail")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<? extends ExperimentResponseDto> experimentGetProposalDetail(
            @PathVariable(value = "topic") final String topic
    ) {
        UserAuthenticationDetails userDetails = helper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[ Experiment ] [UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating getProposalDetail API call: [Path: /proposal/{}/detail, Method: GET]", logPrefix, topic);
        log.debug("{}Authenticated User Info: [Username: {}, Role: {}]", logPrefix, userDetails.getUsername(), role);
        log.debug("{}Received Path Variable: [Topic: {}]", logPrefix, topic);

        ExperimentProposalDetailQueryRequestDto dto = ExperimentProposalDetailQueryRequestDto.builder().topic(topic).build();

        ExperimentProposalDetailQueryGrpcResult result = this.processor.getProposalDetail(dto);

        if (!result.getSuccess()) {
            return processor.getfailureResponseEntity(result);
        }

        return processor.getSuccessResponseEntity(dto, result);
    }
}
