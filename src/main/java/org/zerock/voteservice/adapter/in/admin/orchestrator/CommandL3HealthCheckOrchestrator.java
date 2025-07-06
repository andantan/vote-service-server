package org.zerock.voteservice.adapter.in.admin.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandL3HealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL3HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.processor.CommandL3HealthCheckProcessor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL3HealthCheckResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class CommandL3HealthCheckOrchestrator
    extends AbstractOrchestrator<CommandL3HealthCheckAdminRequestDto, ResponseDto> {

    private final CommandL3HealthCheckProcessor commandL3HealthCheckProcessor;

    public CommandL3HealthCheckOrchestrator(
            ControllerHelper controllerHelper,
            CommandL3HealthCheckProcessor commandL3HealthCheckProcessor
    ) {
        super(controllerHelper);
        this.commandL3HealthCheckProcessor = commandL3HealthCheckProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            CommandL3HealthCheckAdminRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting {} for L3 ping: {}", logPrefix, requestDto.command(), requestDto.getPing());

        CommandL3HealthCheckGrpcRequestDto healthRequestDto = CommandL3HealthCheckGrpcRequestDto.builder()
                .ping(requestDto.getPing())
                .build();

        GrpcCommandL3HealthCheckResponseResult healthResult = processStep(
                commandL3HealthCheckProcessor, healthRequestDto, logPrefix, "Command Health Check"
        );

        if (!healthResult.getSuccess()) {
            return createFailureResponse(
                    commandL3HealthCheckProcessor, healthResult, logPrefix, "Command Health Check"
            );
        }

        return createSuccessResponse(
                commandL3HealthCheckProcessor, healthRequestDto, healthResult, logPrefix, "Command Health Check"
        );
    }
}
