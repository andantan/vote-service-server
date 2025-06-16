package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.dto.vote.base.BaseVoteResponse;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 생성 응답 데이터 인터페이스"
)
public class VoteProposalResponseDto extends BaseVoteResponse {
    @JsonProperty("duration")
    @Schema(
            description = "투표 지속 시간 (테스트: 분 단위)",
            example = "60"
    )
    private Integer duration;
}
