package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VoteBallotDto {
    @JsonProperty("hash")
    private String userHash;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("option")
    private String option;
}