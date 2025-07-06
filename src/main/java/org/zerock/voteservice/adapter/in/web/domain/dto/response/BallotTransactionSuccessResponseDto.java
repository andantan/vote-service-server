package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BallotTransactionSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("option")
    private String option;

    @JsonProperty("vote_hash")
    private String voteHash;
}
