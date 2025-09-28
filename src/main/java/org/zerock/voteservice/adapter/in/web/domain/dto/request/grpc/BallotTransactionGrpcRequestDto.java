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
public class BallotTransactionGrpcRequestDto implements GrpcRequestDto {
    private String userHash;
    private String topic;
    private String option;
    private String salt;
}
