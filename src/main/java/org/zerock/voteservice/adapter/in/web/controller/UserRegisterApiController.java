package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserRegisterWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.UserCreateOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
@Log4j2
@RestController
public class UserRegisterApiController extends UserApiEndpointMapper {

    private final UserCreateOrchestrator userCreateOrchestrator;

    public UserRegisterApiController(UserCreateOrchestrator userCreateOrchestrator) {
        this.userCreateOrchestrator = userCreateOrchestrator;
    }

    @PostMapping("/register")
    public ResponseEntity<? extends ResponseDto> register(
            @RequestBody UserRegisterWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to UserCreateOrchestrator.");

        return userCreateOrchestrator.orchestrate(dto);
    }
}
