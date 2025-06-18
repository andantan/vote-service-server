package org.zerock.voteservice.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.dto.query.Base.BaseQueryRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표 현황 또는 결과 조회 요청 데이터 인터페이스"
)
public class ProposalQueryRequestDto extends BaseQueryRequest {
    @JsonProperty("topic")
    @Schema(
            description = "조회할 투표 주제",
            example = "원격 근무 활성화를 위한 제도 개선"
    )
    private String topic;
}
