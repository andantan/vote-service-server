package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VoteProposalDto {
    @JsonProperty("topic")
    private String topic;

    @JsonProperty("duration")
    private int duration;
}
