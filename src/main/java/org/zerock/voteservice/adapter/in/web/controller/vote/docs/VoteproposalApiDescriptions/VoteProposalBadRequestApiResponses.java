package org.zerock.voteservice.adapter.in.web.controller.vote.docs.VoteproposalApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "투표 제안 실패 (잘못된 요청 데이터: 이미 진행되었던 투표, 유효하지 않은 옵션 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "이미 진행되었던 투표",
                                        summary = "PROPOSAL_EXPIRED 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "이미 진행되었던 투표입니다.",
                                                  "status": "PROPOSAL_EXPIRED",
                                                  "http_status_code": 400
                                                }"""
                                )
                        }
                )
        )
})
public @interface VoteProposalBadRequestApiResponses { }
