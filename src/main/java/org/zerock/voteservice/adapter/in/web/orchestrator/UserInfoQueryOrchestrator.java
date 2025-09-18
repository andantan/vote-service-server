package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserInfoWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserInfoRequestDto;
import org.zerock.voteservice.adapter.in.web.service.UserInfoService;
import org.zerock.voteservice.adapter.in.web.service.UserInfoServiceResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class UserInfoQueryOrchestrator extends AbstractOrchestrator<UserInfoWebClientRequestDto, ResponseDto> {
    private final UserInfoService userInfoService;

    protected UserInfoQueryOrchestrator(
            ControllerHelper controllerHelper,
            UserInfoService userInfoService
    ) {
        super(controllerHelper);
        this.userInfoService = userInfoService;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            UserInfoWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting query user info for username: {}", logPrefix, requestDto.getUsername());

        UserInfoRequestDto userInfoRequestDto = UserInfoRequestDto.builder()
                .uid(requestDto.getUid())
                .username(requestDto.getUsername())
                .userhash(requestDto.getUserhash())
                .build();

        UserInfoServiceResult result = this.userInfoService.getUserInfo(userInfoRequestDto);

        if (!result.getSuccess()) {
            return this.userInfoService.getFailureResponseEntity(result);
        }

        log.debug("{}User successfully quried info: [UserHash: {}]",
                logPrefix, result.getUserHash());

        return this.userInfoService.getSuccessResponseEntity(result);
    }
}
