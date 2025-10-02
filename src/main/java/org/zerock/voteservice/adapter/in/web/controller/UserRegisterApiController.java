package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserEmailVerificationWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserRegisterWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserCreateOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserVerificationOrchestrator;

@Log4j2
@RestController
@RequiredArgsConstructor
@Tag(name = "회원 가입", description = "회원 가입 작업 API")
public class UserRegisterApiController extends UserApiEndpointMapper {

    private final UserCreateOrchestrator userCreateOrchestrator;
    private final UserVerificationOrchestrator userVerificationOrchestrator;

    @PostMapping("/register")
    @Operation(summary = "회원 가입 요청", description = "회원 개인 정보를 통한 가입 요청")
    public ResponseEntity<? extends ResponseDto> register(
            @RequestBody UserRegisterWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /register request. Delegating to UserCreateOrchestrator.");

        return userCreateOrchestrator.orchestrate(dto);
    }

    @PostMapping("/email-verification")
    @Operation(summary = "회원 이메일 인증 요청", description = "회원 이메일로 인증 문자 전송 요청")
    public ResponseEntity<? extends ResponseDto> sendTestMail(
            @RequestBody UserEmailVerificationWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /email-verification request. Delegating to UserVerificationOrchestrator.");

        return this.userVerificationOrchestrator.orchestrate(dto);

    }
}
