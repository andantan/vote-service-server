package org.zerock.voteservice.controller.docs.proposalApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.dto.vote.VoteErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류로 인한 투표 제안 실패",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "알 수 없는 오류",
                                        summary = "UNKNOWN_ERROR 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "알 수 없는 오류가 발생했습니다.",
                                                  "status": "UNKNOWN_ERROR",
                                                  "http_status_code": 500
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "캐시 서버 접근 오류",
                                        summary = "CACHE_ACCESS_ERROR 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "캐시 서버에서 알 수 없는 오류가 발생했습니다.",
                                                  "status": "CACHE_ACCESS_ERROR",
                                                  "http_status_code": 500
                                                }"""
                                )
                        }
                )
        )
})
public @interface ProposalInternalServerErrorApiResponses { }
