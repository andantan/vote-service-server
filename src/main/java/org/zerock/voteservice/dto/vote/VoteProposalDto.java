package org.zerock.voteservice.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Schema(description = "투표 생성 요청 데이터 인터페이스")
@Data
public class VoteProposalDto {
    @Schema(
            description = "투표의 주제 (예: 법률 개정안 찬반 투표)",
            example = "법률 개정안 찬반 투표",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("topic")
    private String topic;

    @Schema(
            description = "투표 지속 시간 (테스트: 분 단위)",
            example = "60",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("duration")
    private int duration;
}
