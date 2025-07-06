package org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.UnauthenticatedRequestDto;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PendingExpiredEventBlockchainRequestDto implements UnauthenticatedRequestDto {
    @JsonProperty("vote_id")
    private String voteId;

    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("vote_options")
    private Map<String, Integer> voteOptions;

    @Override
    public String identifier() {
        return String.format("ExpiredPendingEvent:%s", voteId);
    }
}
