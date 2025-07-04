package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.voteproposalApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.domain.dto.vote.error.VoteErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "409",
                description = "투표 제안 실패 (이미 진행 중인 투표)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "현재 진행 중인 투표",
                                summary = "PROPOSAL_ALREADY_OPEN 오류",
                                value = """
                                        {
                                          "success": false,
                                          "message": "현재 진행 중인 투표입니다.",
                                          "grpcServerStatus": "PROPOSAL_ALREADY_OPEN",
                                          "http_status_code": 409
                                        }"""
                        )
                )
        )
})
public @interface VoteProposalConflictApiResponses { }