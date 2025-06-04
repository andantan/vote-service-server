package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VoteSubmitDto {
    @JsonProperty("hash")
    private String hash;

    @JsonProperty("option")
    private String option;

    @JsonProperty("topic")
    private String topic;
}