package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserAuthenticationWebClientRequestDto;

@Log4j2
@RestController
@Tag(name = "회원 로그인", description = "회원 로그인 작업 API")
public class UserApiLoginController extends UserApiEndpointMapper {

    @PostMapping("/login")
    @Operation(summary = "회원 인증 요청", description = "회원 개인 정보를 통한 인증 요청")
    public ResponseEntity<? extends ResponseDto> login(
            @RequestBody UserAuthenticationWebClientRequestDto dto
    ) {
        return null;
    }
}
