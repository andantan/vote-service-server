package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.RequestDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalCachingRequestDto implements RequestDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("options")
    private List<String> options;
}
