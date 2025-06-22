package org.zerock.voteservice.adapter.in.web.dto.vote.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseVoteRequest {
    @JsonProperty("topic")
    @Schema(
            description = "투표의 주제",
            example = "법률 개정안 찬반 투표",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String topic;
}
