package org.zerock.voteservice.adapter.in.web.dto.query.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.query.Base.BaseQueryResponse;
import org.zerock.voteservice.adapter.in.web.dto.query.schema.ProposalResponseSchema;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 현황 또는 결과 리스트 조회 요청 데이터 인터페이스"
)
public class QueryProposalFilteredListResponseDto extends BaseQueryResponse {
    @JsonProperty("summarize")
    @Schema(
            description = """
                    투표의 요약 여부
                     - true: 요약된 리스트 반환
                     - false: 각 투표의 디테일을 모두 포함된 리스트 반환
                    """,
            example = "true",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean summarize = false;

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
            implementation = Boolean.class
    )
    private Boolean expired = null;

    @JsonProperty("sort_order")
    @Schema(
            description = """
                    투표 조회 리스트 나열 순서
                     - asc: Filter [ 오름차순: expiredAt 기준 ]
                     - desc: Filter [ 내림차순: expiredAt 기준 ]
                     - null: Non-Filter [ 순서 상관 없이 모든 투표 반환 ]
                    """,
            example = "true",
            type = "string",
            implementation = String.class
    )
    private String sortOrder;

    @JsonProperty("sort_by")
    @Schema(
            description = """
                    투표 조회 리스트 나열 순서 기준 필드
                     - 사용 가능한 필드는 Schema [ ProposalDetailSchema ] 참조
                    """,
            example = "true",
            type = "string",
            implementation = String.class
    )
    private String sortBy;

    @JsonProperty("skip")
    @Schema(
            description = "조회 결과에서 건너뛸 항목 수 (페이징)",
            example = "0",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer skip = 0;

    @JsonProperty("limit")
    @Schema(
            description = "조회할 최대 항목 수 (페이징)",
            example = "15",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer limit = 15;

    @JsonProperty("proposal_list")
    @ArraySchema(
            arraySchema = @Schema(
                    description = """
                            필터링된 투표 데이터 리스트
                             - summarize=true인 경우: ProposalSummarizedSchema 데이터 인터페이스 참조
                             - summarize=false인 경우: ProposalDetailSchema 데이터 인터페이스 참조
                            """
            ),
            schema = @Schema(
                    description = "투표 데이터 객체",
                    type = "object",
                    implementation = ProposalResponseSchema.class
            )
    )
    private List<? extends ProposalResponseSchema> proposalList;

    @JsonProperty("proposal_list_length")
    @Schema(
            description = "필터링된 투표 데이터 목록 개수",
            example = "22",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer proposalListLength = 0;
}
