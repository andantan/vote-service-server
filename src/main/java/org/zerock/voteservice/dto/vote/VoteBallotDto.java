package org.zerock.voteservice.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Schema(description = "투표지 등록 요청 데이터 인터페이스")
@Data
public class VoteBallotDto {
    @JsonProperty("hash")
    @Schema(
            description = "사용자를 식별하는 고유 해시값 = Sha256(\"이름\"|\"이메일\"|\"전화번호\"|\"성별\"|\"생년월일\")",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userHash;

    @JsonProperty("topic")
    @Schema(
            description = "투표의 주제 (예: 법률 개정안 찬반 투표)",
            example = "법률 개정안 찬반 투표",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String topic;

    @JsonProperty("option")
    @Schema(
            description = "투표 선택 항목",
            example = "찬성",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String option;
}