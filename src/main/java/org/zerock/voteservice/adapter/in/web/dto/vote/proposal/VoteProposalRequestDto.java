package org.zerock.voteservice.adapter.in.web.dto.vote.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.vote.base.BaseVoteRequest;

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
            type = "integer",
            format = "int32",
            implementation = Integer.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer duration;

    @JsonProperty("options")
    @ArraySchema(
            arraySchema = @Schema(
                    description = "투표 가능한 선택지 목록",
                    example = "[\"찬성\", \"반대\", \"기권\"]",
                    requiredMode = Schema.RequiredMode.REQUIRED
            ),
            schema = @Schema(
                    description = "선택 가능한 옵션",
                    type = "string",
                    implementation = String.class
            )
    )
    private List<String> options;
}
