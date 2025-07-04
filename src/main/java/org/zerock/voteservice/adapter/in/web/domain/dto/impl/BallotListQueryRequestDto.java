package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.zerock.voteservice.adapter.in.web.domain.dto.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallotListQueryRequestDto implements RequestDto {
    @JsonProperty("user_hash")
    private String userHash;
}
