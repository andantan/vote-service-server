package org.zerock.voteservice.adapter.in.web.controller.docs.submit.voteSubmitApiDescriptions;

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
                responseCode = "409",
                description = "이미 투표에 참여하여 재투표가 불가능함",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "중복 투표",
                                summary = "DUPLICATE_VOTE_SUBMISSION 오류",
                                value = """
                                        {
                                          "success": false,
                                          "message": "이미 참가한 투표입니다. (재투표 불가)",
                                          "status": "DUPLICATE_VOTE_SUBMISSION",
                                          "http_status_code": 409
                                        }"""
                        )
                )
        )
})
public @interface VoteSubmitConflictApiResponses { }