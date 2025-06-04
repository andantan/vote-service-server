package org.zerock.voteservice.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockCreatedEventDto {
    @JsonProperty("voting_id")
    private String votingId;

    @JsonProperty("height")
    private int height;
}
