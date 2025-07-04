package org.zerock.voteservice.adapter.in.web.domain.dto.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BallotCachingSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("vote_hash")
    private String voteHash;

    @JsonProperty("option")
    private String option;
}
