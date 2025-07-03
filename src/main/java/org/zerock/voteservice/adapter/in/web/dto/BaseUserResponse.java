package org.zerock.voteservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUserResponse implements ResponseDto {
    @JsonProperty("success")
    @Schema(
            description = "유저 API 요청 처리 여부",
            example = "true",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean success;

    @JsonProperty("message")
    @Schema(
            description = "응답 메세지",
            example = "회원 가입이 완료되었습니다.",
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

    @JsonProperty("uid")
    @Schema(
            description = "유저의 고유한 ID값",
            example = "2693314",
            type = "integer",
            format = "int32",
            implementation = Integer.class
    )
    private Integer uid;

    @JsonProperty("user_hash")
    @Schema(
            description = "유저의 고유한 Hash값",
            example = "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
            type = "string",
            implementation = String.class
    )
    private String userHash;
}
