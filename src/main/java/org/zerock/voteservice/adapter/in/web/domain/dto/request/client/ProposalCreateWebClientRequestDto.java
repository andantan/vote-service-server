package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalCreateWebClientRequestDto implements RestApiRequestDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("options")
    private List<String> options;
}
