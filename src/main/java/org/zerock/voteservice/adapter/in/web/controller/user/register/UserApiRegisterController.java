package org.zerock.voteservice.adapter.in.web.controller.user.register;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.user.mapper.UserApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.user.service.UserRegisterService;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;

@Log4j2
@RestController
public class UserApiRegisterController extends UserApiEndpointMapper {

    private final UserRegisterService userRegisterService;

    public UserApiRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<? extends ResponseDto> register(@RequestBody UserRegisterRequestDto dto) {
        userRegisterService.register(dto);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
