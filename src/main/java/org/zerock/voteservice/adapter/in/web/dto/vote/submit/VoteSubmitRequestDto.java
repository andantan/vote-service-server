package org.zerock.voteservice.adapter.in.web.dto.vote.submit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.vote.base.BaseVoteRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표지 등록 요청 데이터 인터페이스"
)
public class VoteSubmitRequestDto extends BaseVoteRequest {
    @JsonProperty("option")
    @Schema(
            description = "투표 선택 항목",
            example = "찬성",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String option;
}