package org.zerock.voteservice.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.zerock.voteservice.dto.query.Base.BaseQueryResponse;
import org.zerock.voteservice.dto.query.schema.BallotSchema;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "유저의 투표 기록 조회 응답 데이터 인터페이스"
)
public class BallotQueryResponseDto extends BaseQueryResponse {
    @JsonProperty("ballots")
    @Schema(description = "사용자가 참여한 투표 기록 목록")
    private List<BallotSchema> ballots;
}
