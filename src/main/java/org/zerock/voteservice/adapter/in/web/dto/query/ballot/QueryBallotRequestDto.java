package org.zerock.voteservice.adapter.in.web.dto.query.ballot;

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
        description = "유저의 투표 기록 조회 요청 데이터 인터페이스"
)
public class QueryBallotRequestDto extends BaseQueryRequest {
    @JsonProperty("user_hash")
    @Schema(
            description = "조회할 사용자의 고유 해시값",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            type = "string",
            format = "hex",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userHash;
}
