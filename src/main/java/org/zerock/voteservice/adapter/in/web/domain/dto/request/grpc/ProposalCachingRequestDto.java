package org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalCachingRequestDto implements GrpcRequestDto {
    private String topic;
    private Integer duration;
    private List<String> options;
}
