package org.zerock.voteservice.adapter.in.web.controller;

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
public class UserApiLoginController extends UserApiEndpointMapper {

    @PostMapping("/login")
    public ResponseEntity<? extends ResponseDto> login(
            @RequestBody UserAuthenticationWebClientRequestDto dto
    ) {
        return null;
    }
}
