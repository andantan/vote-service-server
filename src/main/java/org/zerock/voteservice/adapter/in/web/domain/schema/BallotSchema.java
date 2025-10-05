package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallotSchema {
    @JsonProperty("vote_hash")
    private String voteHash;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("submitted_at")
    private LocalDateTime submittedAt;
}
