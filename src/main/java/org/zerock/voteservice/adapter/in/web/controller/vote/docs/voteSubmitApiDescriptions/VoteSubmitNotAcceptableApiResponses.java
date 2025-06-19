package org.zerock.voteservice.adapter.in.web.controller.vote.docs.voteSubmitApiDescriptions;

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
                responseCode = "406",
                description = "투표가 이미 마감되어 제출할 수 없음",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "투표 마감",
                                summary = "TIMEOUT_PROPOSAL 오류",
                                value = """
                                        {
                                          "success": false,
                                          "message": "투표가 마감되어 정산 중입니다.",
                                          "status": "TIMEOUT_PROPOSAL",
                                          "http_status_code": 406
                                        }"""
                        )
                )
        )
})
public @interface VoteSubmitNotAcceptableApiResponses { }