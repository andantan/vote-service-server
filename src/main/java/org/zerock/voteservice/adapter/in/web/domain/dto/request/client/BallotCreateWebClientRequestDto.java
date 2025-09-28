package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallotCreateWebClientRequestDto implements RestApiRequestDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("option")
    private String option;
    
    @JsonProperty("salt")
    private String salt;
}
