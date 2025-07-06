package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "투표 데이터(요약 데이터) 인터페이스"
)
public class ProposalSummarizedSchema implements ProposalResponseSchema {
    @JsonProperty("topic")
    @Schema(
            description = """
                    투표의 주제(이름)
                     - 투표 이름은 Primary Key
                     - 모든 투표는 주제(이름)으로 구분
                    """,
            example = "원격 근무 활성화를 위한 제도 개선",
            type = "string",
            implementation = String.class
    )
    private String topic;

    @JsonProperty("expired")
    @Schema(
            description = """
                    투표의 만료 여부
                     - true: 투표 종료(만료됨)
                     - false: 투표 진행 중
                    """,
            example = "true",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean expired;

    @JsonProperty("expired_at")
    @Schema(
            description = """
                    투표 종료 시각
                     - 투표 생성 시각 + 투표 지속 시간
                    """,
            example = "2025-06-14T09:04:45.199",
            type = "string",
            format = "date-time",
            implementation = LocalDateTime.class
    )
    private LocalDateTime expiredAt;
}
