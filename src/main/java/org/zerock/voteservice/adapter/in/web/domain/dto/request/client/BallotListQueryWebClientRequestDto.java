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
public class BallotListQueryWebClientRequestDto implements RestApiRequestDto {
    @JsonProperty("user_hash")
    private String userHash;
}
