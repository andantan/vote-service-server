package org.zerock.voteservice.adapter.in.web.dto.vote.submit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.zerock.voteservice.adapter.in.web.dto.vote.base.BaseVoteResponse;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "투표지 등록 응답 데이터 인터페이스"
)
public class VoteSubmitResponseDto extends BaseVoteResponse {
    @JsonProperty("user_hash")
    @Schema(
            description = "요청한 유저의 고유 해시값(유저 식별)",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            type = "string",
            format = "hex",
            implementation = String.class
    )
    private String userHash;

    @JsonProperty("vote_option")
    @Schema(
            description = "요청한 유저의 선택한 투표 옵션",
            example = "찬성",
            type = "string",
            implementation = String.class
    )
    private String voteOption;

    @JsonProperty("vote_hash")
    @Schema(
            description = "해당 투표 자체의 해시값 = Sha256(\"user_hash\"|\"topic\"|\"option\")",
            example = "aeb4b82ecad02ae8f76762ebeaf5335a121f25501ba0302c77367b663b70e8b3",
            type = "string",
            format = "hex",
            implementation = String.class
    )
    private String voteHash;
}
