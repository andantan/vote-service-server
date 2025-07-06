package org.zerock.voteservice.adapter.in.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.admin.controller.mapper.CommandApiEndPointMapper;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandL3HealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.orchestrator.CommandL3HealthCheckOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CommandApiController extends CommandApiEndPointMapper {

    private final CommandL3HealthCheckOrchestrator L3HealthCheckerOrchestrator;

    @GetMapping("/L3/health/{ping}")
    public ResponseEntity<? extends ResponseDto> L3HealthCheck(
            @PathVariable(value = "ping") String ping
    ) {
        log.debug(">>>>>> Received /L3/health/{} request. Delegating to CommandL3HealthCheckOrchestrator.", ping);

        CommandL3HealthCheckAdminRequestDto requestDto = CommandL3HealthCheckAdminRequestDto.builder().ping(ping).build();

        return L3HealthCheckerOrchestrator.orchestrate(requestDto);
    }
}
