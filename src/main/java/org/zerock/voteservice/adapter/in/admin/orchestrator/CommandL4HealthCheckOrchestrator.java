package org.zerock.voteservice.adapter.in.admin.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandL4HealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL4HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.processor.CommandL4HealthCheckProcessor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL4HealthCheckResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class CommandL4HealthCheckOrchestrator
    extends AbstractOrchestrator<CommandL4HealthCheckAdminRequestDto, ResponseDto> {

    private final CommandL4HealthCheckProcessor commandL4HealthCheckProcessor;

    public CommandL4HealthCheckOrchestrator(
            ControllerHelper controllerHelper,
            CommandL4HealthCheckProcessor commandL4HealthCheckProcessor
    ) {
        super(controllerHelper);
        this.commandL4HealthCheckProcessor = commandL4HealthCheckProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            CommandL4HealthCheckAdminRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting {} for L4 HealthCheck: {}", logPrefix, requestDto.command(), requestDto.getPing());

        CommandL4HealthCheckGrpcRequestDto healthRequestDto = CommandL4HealthCheckGrpcRequestDto.builder()
                .ping(requestDto.getPing())
                .build();

        GrpcCommandL4HealthCheckResponseResult healthResult = processStep(
                commandL4HealthCheckProcessor, healthRequestDto, logPrefix, "Command Health Check L4"
        );

        if (!healthResult.getSuccess()) {
            return createFailureResponse(
                    commandL4HealthCheckProcessor, healthResult, logPrefix, "Command Health Check L4"
            );
        }

        return createSuccessResponse(
                commandL4HealthCheckProcessor, healthRequestDto, healthResult, logPrefix, "Command Health Check L4"
        );

    }
}
