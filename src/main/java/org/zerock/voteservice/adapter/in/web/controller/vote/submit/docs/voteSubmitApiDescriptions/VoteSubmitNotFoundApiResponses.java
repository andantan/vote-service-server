package org.zerock.voteservice.adapter.in.web.controller.vote.submit.docs.voteSubmitApiDescriptions;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "404",
                description = "요청한 투표를 찾을 수 없음",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "투표가 진행 중이 아님",
                                summary = "PROPOSAL_NOT_OPEN 오류",
                                value = """
                                        {
                                          "success": false,
                                          "message": "현재 존재하지 않는 투표입니다.",
                                          "status": "PROPOSAL_NOT_OPEN",
                                          "http_status_code": 404
                                        }"""
                        )
                )
        )
})
public @interface VoteSubmitNotFoundApiResponses { }
