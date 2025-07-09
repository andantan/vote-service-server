package org.zerock.voteservice.adapter.in.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.admin.controller.mapper.CommandApiEndPointMapper;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandL3HealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandL4HealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.orchestrator.CommandL3HealthCheckOrchestrator;
import org.zerock.voteservice.adapter.in.admin.orchestrator.CommandL4HealthCheckOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 전용", description = "관리자 명령어 작업 API")
public class CommandApiController extends CommandApiEndPointMapper {

    private final CommandL3HealthCheckOrchestrator L3HealthCheckerOrchestrator;
    private final CommandL4HealthCheckOrchestrator L4HealthCheckerOrchestrator;

    @Operation(summary = "L3 상태 확인", description = "L3에 ping(string)을 보내고 이를 반환(pong)하며 서버 네트워크 상태 응답")
    @GetMapping("/L3/health/{ping}")
    public ResponseEntity<? extends ResponseDto> L3HealthCheck(
            @PathVariable(value = "ping") String ping
    ) {
        log.debug(">>>>>> Received /L3/health/{} request. Delegating to CommandL3HealthCheckOrchestrator.", ping);

        CommandL3HealthCheckAdminRequestDto requestDto = CommandL3HealthCheckAdminRequestDto.builder().ping(ping).build();

        return L3HealthCheckerOrchestrator.orchestrate(requestDto);
    }

    @Operation(summary = "L4 상태 확인", description = "L4에 ping(string)을 보내고 이를 반환(pong)하며 서버 네트워크 상태 응답")
    @GetMapping("/L4/health/{ping}")
    public ResponseEntity<? extends ResponseDto> L34ealthCheck(
            @PathVariable(value = "ping") String ping
    ) {
        log.debug(">>>>>> Received /L4/health/{} request. Delegating to CommandL4HealthCheckOrchestrator.", ping);

        CommandL4HealthCheckAdminRequestDto requestDto = CommandL4HealthCheckAdminRequestDto.builder().ping(ping).build();

        return L4HealthCheckerOrchestrator.orchestrate(requestDto);
    }
}
