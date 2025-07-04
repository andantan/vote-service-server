package org.zerock.voteservice.adapter.in.web.domain.dto.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "투표 데이터 인터페이스"
)
public class ProposalDetailSchema implements ProposalResponseSchema {
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

    @JsonProperty("duration")
    @Schema(
            description = "투표의 총 진행 기간",
            example = "9",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer duration;

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

    @JsonProperty("block_heights")
    @ArraySchema(
            arraySchema = @Schema(
                    description = """
                            투표지와 관련된 블록 정보 목록
                             - BlockSchema 데이터 인터페이스 참조
                            """
            ),
            schema = @Schema(
                    description = "투표지와 관련된 블록 정보 객체",
                    type = "object",
                    implementation = BlockHeightSchema.class
            )
    )
    private List<BlockHeightSchema> blockHeights;

    @JsonProperty("result")
    @Schema(
            description = """
                    투표의 최종 집계 결과
                     - ResultSchema 데이터 인터페이스 참조
                    """,
            type = "object",
            implementation = ResultSchema.class
    )
    private ResultSchema result;

    @JsonProperty("created_at")
    @Schema(
            description = "투표가 생성된 시각",
            example = "2025-06-14T08:55:45.199",
            type = "string",
            format = "date-time",
            implementation = LocalDateTime.class
    )
    private LocalDateTime createdAt;

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

    @JsonProperty("options")
    @ArraySchema(
            arraySchema = @Schema(
                    description = "해당 투표에서 선택 가능한 옵션들의 목록",
                    example = "[ \"1\", \"2\", \"3\", \"4\", \"5\" ]"
            ),
            schema = @Schema(
                    description = "선택 가능한 옵션",
                    type = "string",
                    implementation = String.class
            )
    )
    private List<String> options;
}
