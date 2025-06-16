package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.dto.vote.base.BaseVoteRequest;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 생성 요청 데이터 인터페이스"
)
public class VoteProposalRequestDto extends BaseVoteRequest {
    @JsonProperty("duration")
    @Schema(
            description = "투표 지속 시간 (테스트: 분 단위)",
            example = "60",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int duration;

    @JsonProperty("options")
    @Schema(
            description = "투표 가능한 선택지 목록",
            example = "[\"찬성\", \"반대\", \"기권\"]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<String> options;
}
