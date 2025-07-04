package org.zerock.voteservice.adapter.in.web.domain.dto;

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
public abstract class BaseQueryResponse implements ResponseDto {
    @JsonProperty("success")
    @Schema(
            description = "조회 API 요청 처리 여부",
            example = "true",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean success;

    @JsonProperty("message")
    @Schema(
            description = "응답 메세지",
            example = "조회가 완료 되었습니다.",
            type = "string",
            implementation = String.class
    )
    private String message;

    @JsonProperty("status")
    @Schema(
            description = "요청 처리 내부 응답 상태 코드",
            example = "OK",
            type = "string",
            implementation = String.class
    )
    private String status;

    @JsonProperty("http_status_code")
    @Schema(
            description = "HTTP 응답 상태 코드",
            example = "200",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer httpStatusCode;
}
