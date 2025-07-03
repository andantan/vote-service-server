package org.zerock.voteservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.schema.ProposalDetailSchema;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 현황 또는 결과 조회 요청 데이터 인터페이스"
)
public class QueryProposalDetailResponseDto extends BaseQueryResponse {
    @JsonProperty("topic")
    @Schema(
            description = "조회 대상 투표 주제(이름)",
            example = "원격 근무 활성화를 위한 제도 개선",
            type = "string",
            implementation = String.class
    )
    private String topic;

    @JsonProperty("proposal")
    @Schema(
            description = """
                            투표 상세 데이터 인터페이스
                             - ProposalDetailSchema 데이터 인터페이스 참조
                            """,
            type = "object",
            implementation = ProposalDetailSchema.class
    )
    private ProposalDetailSchema proposal;
}
