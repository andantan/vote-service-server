package org.zerock.voteservice.dto.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.dto.vote.base.BaseVoteRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표지 등록 요청 데이터 인터페이스"
)
public class VoteBallotRequestDto extends BaseVoteRequest {
    @JsonProperty("option")
    @Schema(
            description = "투표 선택 항목",
            example = "찬성",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String option;

    @JsonProperty("hash")
    @Schema(
            description = "요청하는 유저의 고유 해시값(유저 식별) = Sha256(\"이름\"|\"이메일\"|\"전화번호\"|\"성별\"|\"생년월일\")",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userHash;
}