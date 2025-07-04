package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.zerock.voteservice.adapter.in.web.domain.dto.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDetailQueryRequestDto implements RequestDto {
    @JsonProperty("topic")
    private String topic;
}
