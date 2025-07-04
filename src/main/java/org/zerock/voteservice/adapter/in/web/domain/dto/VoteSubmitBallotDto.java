package org.zerock.voteservice.adapter.in.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteSubmitBallotDto {
    private String userHash;
    private String topic;
    private String option;
}
