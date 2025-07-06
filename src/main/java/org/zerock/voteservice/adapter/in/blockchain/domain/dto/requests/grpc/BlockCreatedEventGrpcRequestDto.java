package org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc;

import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

@Getter
@Builder
public class BlockCreatedEventGrpcRequestDto implements GrpcRequestDto {
    private String topic;
    private int txCount;
    private int height;
}
