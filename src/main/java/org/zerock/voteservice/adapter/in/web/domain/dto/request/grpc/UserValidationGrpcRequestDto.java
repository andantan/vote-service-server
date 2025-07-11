package org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationGrpcRequestDto implements GrpcRequestDto {
    private Integer uid;
    private String userHash;
}
