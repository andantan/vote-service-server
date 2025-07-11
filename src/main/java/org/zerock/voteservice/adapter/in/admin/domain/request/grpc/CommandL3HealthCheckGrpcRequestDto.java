package org.zerock.voteservice.adapter.in.admin.domain.request.grpc;

import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

@Getter
@Builder
public class CommandL3HealthCheckGrpcRequestDto implements GrpcRequestDto {
    private String ping;
}
