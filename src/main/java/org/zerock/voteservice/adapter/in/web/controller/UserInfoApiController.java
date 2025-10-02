package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserInfoWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserPasswordModifyWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserPasswordResetWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserInfoQueryOrchestrator;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserModifyPasswordOrchestrator;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserResetPasswordOrchestrator;

@Log4j2
@RestController
@Tag(name = "회원 정보 관리", description = "회원 정보 변경 및 조회 작업 API")
public class UserInfoApiController extends UserApiEndpointMapper {

    private final UserInfoQueryOrchestrator userInfoQueryOrchestrator;
    private final UserModifyPasswordOrchestrator userModifyPasswordOrchestrator;
    private final UserResetPasswordOrchestrator userResetPasswordOrchestrator;

    public UserInfoApiController(
            UserInfoQueryOrchestrator userInfoQueryOrchestrator,
            UserModifyPasswordOrchestrator userModifyPasswordOrchestrator,
            UserResetPasswordOrchestrator userResetPasswordOrchestrator
    ) {
        this.userInfoQueryOrchestrator = userInfoQueryOrchestrator;
        this.userModifyPasswordOrchestrator = userModifyPasswordOrchestrator;
        this.userResetPasswordOrchestrator = userResetPasswordOrchestrator;
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

    @Operation(summary = "회원 비밀번호 변경", description = "UID, username을 Primary key로 하여 회원 비밀번호 변경")
    @PutMapping("/modify/user-password")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> modifyUserPassword(
            @RequestBody UserPasswordModifyWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /modify/user-password request. Delegating to UserModifyPasswordOrchestrator.");

        return this.userModifyPasswordOrchestrator.orchestrate(dto);
    }

    @Operation(summary = "회원 비밀번호 초기화", description = "비밀번호 초기화를 위한 이메일 인증 요청")
    @PutMapping("/reset/user-password")
    public ResponseEntity<? extends ResponseDto> resetUserPassword(
            @RequestBody UserPasswordResetWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /reset/user-password request. Delegating to UserResetPasswordOrchestrator.");

        return this.userResetPasswordOrchestrator.orchestrate(dto);
    }
}
