package org.zerock.voteservice.adapter.in.web.dto.query.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.query.Base.BaseQueryRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 현황 또는 결과 리스트 조회 요청 데이터 인터페이스"
)
public class QueryProposalFilteredListRequestDto extends BaseQueryRequest {
    @JsonProperty("summarize")
    @Schema(
            description = """
                    투표의 요약 여부
                     - true: 요약된 리스트 반환
                     - false: 각 투표의 디테일을 모두 포함된 리스트 반환
                    """,
            example = "true",
            type = "boolean",
            implementation = Boolean.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean summarize;

    @JsonProperty("expired")
    @Schema(
            description = """
                    투표의 만료 여부(조회 하는 시점 기준)
                     - true: Filter [ 종료된 투표 ]
                     - false: Filter [ 현재 진행 중인 투표 ]
                     - null: Non-Filter [ 진행 여부 상관 없이 모든 투표 반환 ]
                    """,
            example = "true",
            type = "boolean",
            nullable = true,
            implementation = Boolean.class
    )
    private Boolean expired = null;

    @JsonProperty("skip")
    @Schema(
            description = "조회 결과에서 건너뛸 항목 수 (페이징)",
            example = "0",
            type = "integer",
            format = "int32",
            implementation = Integer.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer skip;

    @JsonProperty("limit")
    @Schema(
            description = "조회할 최대 항목 수 (페이징)",
            example = "15",
            type = "integer",
            format = "int32",
            implementation = Integer.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer limit;

    public Boolean isExpiredFiltered() {
        return this.expired != null;
    }
}
