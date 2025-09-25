package org.zerock.voteservice.adapter.in.admin.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.admin.domain.request.client.CommandIntegratedHealthCheckAdminRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL3HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL4HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.response.CommandIntegratedHealthCheckSuccessResponseDto;
import org.zerock.voteservice.adapter.in.admin.processor.CommandL3HealthCheckProcessor;
import org.zerock.voteservice.adapter.in.admin.processor.CommandL4HealthCheckProcessor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL3HealthCheckResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL4HealthCheckResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class CommandIntegratedHealthCheckOrchestrator
        extends AbstractOrchestrator<CommandIntegratedHealthCheckAdminRequestDto, ResponseDto> {

    private final CommandL3HealthCheckProcessor commandL3HealthCheckProcessor;
    private final CommandL4HealthCheckProcessor commandL4HealthCheckProcessor;

    protected CommandIntegratedHealthCheckOrchestrator(
            ControllerHelper controllerHelper,
            CommandL3HealthCheckProcessor commandL3HealthCheckProcessor,
            CommandL4HealthCheckProcessor commandL4HealthCheckProcessor
    ) {
        super(controllerHelper);
        this.commandL3HealthCheckProcessor = commandL3HealthCheckProcessor;
        this.commandL4HealthCheckProcessor = commandL4HealthCheckProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            CommandIntegratedHealthCheckAdminRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting {} for Integrated HealthCheck: {}", logPrefix, requestDto.command(), requestDto.getPing());

        CommandL3HealthCheckGrpcRequestDto l3HealthRequestDto = CommandL3HealthCheckGrpcRequestDto.builder()
                .ping(requestDto.getPing())
                .build();

        GrpcCommandL3HealthCheckResponseResult l3HealthResult = processStep(
                commandL3HealthCheckProcessor, l3HealthRequestDto, logPrefix, "Command Health Check L3(Integrated)"
        );

        if (!l3HealthResult.getSuccess()) {
            return createFailureResponse(
                    commandL3HealthCheckProcessor, l3HealthResult, logPrefix, "Command Health Check L3(Integrated)"
            );
        }

        CommandL4HealthCheckGrpcRequestDto l4HealthRequestDto = CommandL4HealthCheckGrpcRequestDto.builder()
                .ping(requestDto.getPing())
                .build();

        GrpcCommandL4HealthCheckResponseResult l4HealthResult = processStep(
                commandL4HealthCheckProcessor, l4HealthRequestDto, logPrefix, "Command Health Check L4(Integrated)"
        );

        if (!l4HealthResult.getSuccess()) {
            return createFailureResponse(
                    commandL4HealthCheckProcessor, l4HealthResult, logPrefix, "Command Health Check L4(Integrated)"
            );
        }

        return this.getSuccessResponseEntity(
                requestDto, l3HealthResult, l4HealthResult, logPrefix
        );
    }

    private ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            CommandIntegratedHealthCheckAdminRequestDto dto,
            GrpcCommandL3HealthCheckResponseResult l3Result,
            GrpcCommandL4HealthCheckResponseResult l4Result,
            String logPrefix
    ) {
        log.info("{}{} SUCCESS.", logPrefix, "Command Health Check Integrated");

        CommandIntegratedHealthCheckSuccessResponseDto successDto = CommandIntegratedHealthCheckSuccessResponseDto.builder()
                .successL3(l3Result.getSuccess())
                .messageL3(l3Result.getMessage())
                .statusL3(l3Result.getStatus())
                .httpStatusCodeL3(l3Result.getHttpStatusCode())
                .pingL3(dto.getPing())
                .pongL3(l3Result.getData().getPong())
                .ipL3(l3Result.getData().getIp())
                .portsL3(l3Result.getData().getPorts())
                .successL4(l4Result.getSuccess())
                .messageL4(l4Result.getMessage())
                .statusL4(l4Result.getStatus())
                .httpStatusCodeL4(l4Result.getHttpStatusCode())
                .pingL4(dto.getPing())
                .pongL4(l4Result.getData().getPong())
                .ipL4(l4Result.getData().getIp())
                .portsL4(l4Result.getData().getPorts())
                .build();

        Integer statusCode;

        if (l3Result.getHttpStatusCode() <= l4Result.getHttpStatusCode()) {
            statusCode = l4Result.getHttpStatusCode();
        } else {
            statusCode = l3Result.getHttpStatusCode();
        }

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(statusCode));
    }
}
