package org.zerock.voteservice.adapter.in.web.controller.docs.ballot.ballotQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.query.error.QueryErrorResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = """
                            잘못된 요청 (예: 유효하지 않은 userHash 형식)
                            
                             - userHash가 null인 경우
                             - userHash의 길이가 64가 아닌 경우
                            """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "잘못된 요청 예시 1",
                                        summary = "userHash가 null인 경우",
                                        value = """
                                        {
                                          "success": false,
                                          "message": "유효하지 않은 해시입니다.",
                                          "grpcServerStatus": "DECODE_ERROR",
                                          "http_status_code": 400
                                        }"""
                                ),
                                @ExampleObject(
                                        name = "잘못된 요청 예시 2",
                                        summary = "userHash의 길이가 64가 아닌 경우",
                                        value = """
                                        {
                                          "success": false,
                                          "message": "유효하지 않은 해시 길이입니다.",
                                          "grpcServerStatus": "INVALID_HASH_LENGTH",
                                          "http_status_code": 400
                                        }"""
                                )
                        }
                )
        ),
})
public @interface QueryBallotBadRequestApiResponses {
}
