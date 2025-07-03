package org.zerock.voteservice.adapter.in.web.dto.vote.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.status.VoteBallotErrorStatus;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.status.VoteProposalErrorStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Schema(
        description = "Vote API 오류 응답 공통 형식"
)
public class VoteErrorResponseDto implements ResponseDto {
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
            example = "비정상적인 투표 선택 사항입니다.",
            type = "string",
            implementation = String.class
    )
    private String message;

    @JsonProperty("status")
    @Schema(
            description = "내부 오류 코드",
            example = "INVALID_OPTION",
            type = "string",
            implementation = String.class
    )
    private String status;

    @JsonProperty("http_status_code")
    @Schema(
            description = "HTTP 응답 상태 코드",
            example = "400",
            type = "number",
            implementation = Integer.class
    )
    private Integer httpStatusCode;

    public static VoteErrorResponseDto from(VoteBallotErrorStatus errorStatus) {
        return VoteErrorResponseDto.builder()
                .success(false)
                .message(errorStatus.getMessage())
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }

    public static VoteErrorResponseDto from(VoteProposalErrorStatus errorStatus) {
        return VoteErrorResponseDto.builder()
                .success(false)
                .message(errorStatus.getMessage())
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }
}
