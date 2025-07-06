package org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc;

import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

import java.util.Map;

@Getter
@Builder
public class PendingExpiredEventGrpcRequestDto implements GrpcRequestDto {
    private String voteId;
    private int voteCount;
    private Map<String, Integer> voteOptions;
}
