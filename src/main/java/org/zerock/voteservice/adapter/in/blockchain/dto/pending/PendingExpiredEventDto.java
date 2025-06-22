package org.zerock.voteservice.adapter.in.blockchain.dto.pending;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class PendingExpiredEventDto {
    @JsonProperty("vote_id")
    private String voteId;

    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("vote_options")
    private Map<String, Integer> voteOptions;
}


