package org.zerock.voteservice.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExpiredPendingEventDto {
    @JsonProperty("voting_id")
    private String votingId;

    @JsonProperty("submits_length")
    private int submitsLength;

    @JsonProperty("submits_option")
    private Map<String, Integer> submitsOption;
}


