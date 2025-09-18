package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserInfoWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserInfoQueryOrchestrator;

@Log4j2
@RestController
@Tag(name = "회원 정보 관리", description = "회원 정보 변경 및 조회 작업 API")
public class UserInfoApiController extends UserApiEndpointMapper {

    private final UserInfoQueryOrchestrator userInfoQueryOrchestrator;

    public UserInfoApiController(UserInfoQueryOrchestrator userInfoQueryOrchestrator) {
        this.userInfoQueryOrchestrator = userInfoQueryOrchestrator;
    }

    @Operation(summary = "회원 정보 조회", description = "UID를 Primary key로 하여 회원 세부 정보 조회")
    @GetMapping("/spec")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getUserSpec(
            @RequestParam(name = "uid") Long uid,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "userhash") String userhash
    ) {
        UserInfoWebClientRequestDto dto = UserInfoWebClientRequestDto.builder()
                .uid(uid)
                .username(username)
                .userhash(userhash)
                .build();

        log.debug(">>>>>> Received /spec request. Delegating to UserInfoQueryOrchestrator.");

        return this.userInfoQueryOrchestrator.orchestrate(dto);
    }
}
