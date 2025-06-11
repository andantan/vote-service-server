package org.zerock.voteservice.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExpiredPendingEventDto {
    @JsonProperty("vote_id")
    private String voteId;

    @JsonProperty("vote_count")
    private int voteCount;

    @JsonProperty("vote_options")
    private Map<String, Integer> voteOptions;
}


