package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.UserPasswordModifyWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserPasswordModifyRequestDto;
import org.zerock.voteservice.adapter.in.web.service.UserModifyService;
import org.zerock.voteservice.adapter.in.web.service.UserModifyServiceResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class UserModifyPasswordOrchestrator extends AbstractOrchestrator<UserPasswordModifyWebClientRequestDto, ResponseDto> {

    private final UserModifyService userModifyService;

    public UserModifyPasswordOrchestrator(
            ControllerHelper controllerHelper,
            UserModifyService userModifyService
    ) {
        super(controllerHelper);
        this.userModifyService = userModifyService;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            UserPasswordModifyWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting modify password info for username: {}", logPrefix, requestDto.getUsername());

        if (!userDetails.getUsername().equals(requestDto.getUsername())) {
            log.warn("{}Attempted to modify password info for abnormal username: {}, request username: {}", logPrefix, requestDto.getUsername(), userDetails.getUsername());
            return this.userModifyService.getAbnormalResponseEntity();
        }

        UserPasswordModifyRequestDto userPasswordModifyRequestDto = UserPasswordModifyRequestDto.builder()
                .uid(requestDto.getUid())
                .username(requestDto.getUsername())
                .newPassword(requestDto.getNewPassword())
                .build();

        UserModifyServiceResult result = this.userModifyService.modifyPassword(userPasswordModifyRequestDto);

        if (!result.getSuccess()) {
            return this.userModifyService.getFailureResponseEntity(result);
        }

        log.debug("{}User successfully modified password info: [UserHash: {}]",
                logPrefix, result.getUserhash());

        return this.userModifyService.getSuccessResponseEntity(result);
    }
}
