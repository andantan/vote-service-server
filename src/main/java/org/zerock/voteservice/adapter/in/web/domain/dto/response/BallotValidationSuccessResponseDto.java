package org.zerock.voteservice.adapter.in.web.domain.dto.response;

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
public class BallotValidationSuccessResponseDto extends CommonResponseDto {
    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("topic")
    private String topic;

    @JsonProperty("option")
    private String option;
}
