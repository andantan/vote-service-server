package org.zerock.voteservice.adapter.in.web.controller.user.login;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.user.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.user.login.UserLoginRequestDto;

@Log4j2
@RestController
public class UserApiLoginController extends UserApiEndpointMapper {

    public ResponseEntity<? extends ResponseDto> login(
            @RequestBody UserLoginRequestDto dto
    ) throws Exception {
        return null;
    }
}
