package org.zerock.voteservice.adapter.in.web.domain.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "투표지 데이터 인터페이스"
)
public class BallotSchema {
    @JsonProperty("vote_hash")
    @Schema(
            description = "해당 투표 자체의 해시값 = Sha256(\"user_hash\"|\"topic\"|\"option\")",
            example = "aeb4b82ecad02ae8f76762ebeaf5335a121f25501ba0302c77367b663b70e8b3",
            type = "string",
            format = "hex",
            implementation = String.class
    )
    private String voteHash;

    @JsonProperty("topic")
    @Schema(
            description = "투표 주제",
            example = "법률 개정안 찬반 투표",
            type = "string",
            implementation = String.class
    )
    private String topic;

    @JsonProperty("submitted_at")
    @Schema(
            description = "투표지 제출 시간",
            example = "2025-06-14T08:55:45.199",
            type = "string",
            format = "date-time",
            implementation = LocalDateTime.class
    )
    private LocalDateTime submittedAt;
}
