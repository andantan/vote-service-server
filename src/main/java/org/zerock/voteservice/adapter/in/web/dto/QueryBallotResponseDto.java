package org.zerock.voteservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.zerock.voteservice.adapter.in.web.dto.schema.BallotSchema;

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
public class QueryBallotResponseDto extends BaseQueryResponse {
    @JsonProperty("user_hash")
    @Schema(
            description = "사용자의 고유 해시",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            type = "string",
            format = "hex",
            implementation = String.class
    )
    private String userHash;

    @JsonProperty("ballot_list")
    @ArraySchema(
            arraySchema = @Schema(
                    description = """
                            사용자가 참여한 투표 기록 목록
                             - BallotSchema 데이터 인터페이스 참조
                            """
            ),
            schema = @Schema(
                    description = "투표지 데이터 객체",
                    type = "object",
                    implementation = BallotSchema.class
            )
    )
    private List<BallotSchema> ballotList;

    @JsonProperty("ballot_list_length")
    @Schema(
            description = "사용자가 참여한 총 투표지 개수",
            example = "22",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer ballotListLength;
}
