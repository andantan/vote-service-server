package org.zerock.voteservice.adapter.in.web.domain.dto.query.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.query.error.status.QueryBallotErrorStatus;
import org.zerock.voteservice.adapter.in.web.domain.dto.query.error.status.QueryProposalErrorStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Schema(
        description = "Query API 오류 응답 공통 형식"
)
public class QueryErrorResponseDto implements ResponseDto {
    @JsonProperty("success")
    @Schema(
            description = "요청 처리 성공 여부",
            example = "false",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean success;

    @JsonProperty("message")
    @Schema(
            description = "오류 상세 메시지",
            example = "해당 유권자를 찾을 수 없습니다.",
            type = "string",
            implementation = String.class
    )
    private String message;

    @JsonProperty("status")
    @Schema(
            description = "내부 오류 코드",
            example = "USER_NOT_FOUND",
            type = "string",
            implementation = String.class
    )
    private String status;

    @JsonProperty("http_status_code")
    @Schema(
            description = "HTTP 응답 상태 코드",
            example = "404",
            type = "number",
            implementation = Integer.class
    )
    private Integer httpStatusCode;

    public static QueryErrorResponseDto from(QueryBallotErrorStatus errorStatus) {
        return QueryErrorResponseDto.builder()
                .success(false)
                .message(errorStatus.getMessage())
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }

    public static QueryErrorResponseDto from(QueryProposalErrorStatus errorStatus) {
        return QueryErrorResponseDto.builder()
                .success(false)
                .message(errorStatus.getMessage())
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }
}
