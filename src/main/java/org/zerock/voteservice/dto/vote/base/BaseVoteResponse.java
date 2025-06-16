package org.zerock.voteservice.dto.vote.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseVoteResponse {
    @JsonProperty("success")
    @Schema(
            description = "요청 처리 여부",
            example = "true"
    )
    private Boolean success;

    @JsonProperty("topic")
    @Schema(
            description = "투표 주제",
            example = "법률 개정안 찬반 투표"
    )
    private String topic;

    @JsonProperty("message")
    @Schema(
            description = "응답 메세지",
            example = "투표 등록이 완료되었습니다."
    )
    private String message;

    @JsonProperty("status")
    @Schema(
            description = "요청 처리 내부 응답 상태 코드",
            example = "OK"
    )
    private String status;

    @JsonProperty("http_status_code")
    @Schema(
            description = "HTTP 응답 상태 코드",
            example = "200"
    )
    private Integer httpStatusCode;
}
