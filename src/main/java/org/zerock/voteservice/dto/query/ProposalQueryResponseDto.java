package org.zerock.voteservice.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.dto.query.Base.BaseQueryResponse;
import org.zerock.voteservice.dto.query.schema.BlockHeightSchema;
import org.zerock.voteservice.dto.query.schema.ResultSchema;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 현황 또는 결과 조회 요청 데이터 인터페이스"
)
public class ProposalQueryResponseDto extends BaseQueryResponse {
    @JsonProperty("topic")
    @Schema(
            description = """
                    투표의 주제(이름)
                     - 투표 이름은 Primary Key
                     - 모든 투표는 주제(이름)으로 구분
                    """,
            example = "원격 근무 활성화를 위한 제도 개선"
    )
    private String topic;

    @JsonProperty("duration")
    @Schema(
            description = "투표의 총 진행 기간",
            example = "9"
    )
    private Integer duration;

    @JsonProperty("expired")
    @Schema(
            description = """
                    투표의 만료 여부
                     - true: 투표 종료(만료됨)
                     - false: 투표 진행 중
                    """,
            example = "true"
    )
    private Boolean expired;

    @JsonProperty("block_heights")
    @Schema(
            description = """
                    투표지와 관련된 블록 정보 목록
                     - BlockSchema 데이터 인터페이스 참조
                    """
    )
    private List<BlockHeightSchema> blockHeights;

    @JsonProperty("result")
    @Schema(
            description = """
                    투표의 최종 집계 결과
                     - ResultSchema 데이터 인터페이스 참조
                    """
    )
    private ResultSchema result;

    @JsonProperty("created_at")
    @Schema(
            description = "투표가 생성된 시각",
            example = "2025-06-14T08:55:45.199"
    )
    private LocalDateTime createdAt;

    @JsonProperty("expired_at")
    @Schema(
            description = """
                    투표 종료 시각
                     - 투표 생성 시각 + 투표 지속 시간
                    """,
            example = "2025-06-14T09:04:45.199"
    )
    private LocalDateTime expiredAt;

    @JsonProperty("options")
    @Schema(
            description = "해당 투표에서 선택 가능한 옵션들의 목록",
            example = "[ \"1\", \"2\", \"3\", \"4\", \"5\" ]"
    )
    private List<String> options;
}
