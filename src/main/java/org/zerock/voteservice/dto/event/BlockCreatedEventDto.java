package org.zerock.voteservice.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockCreatedEventDto {
    @JsonProperty("vote_id")
    private String voteId;

    @JsonProperty("height")
    private int height;
}
