package org.zerock.voteservice.adapter.in.web.controller.query.docs.ballotQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryErrorResponseDto;

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
                            잘못된 요청 (예: 유효하지 않은 userHash 형식)\n
                             - userHash가 null인 경우
                             - userHash의 길이가 64가 아닌 경우
                            """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "잘못된 요청 예시",
                                summary = "유효하지 않은 userHash",
                                value = """
                                        {
                                          "success": false,
                                          "message": "유효하지 않은 해시입니다.",
                                          "status": "INVALID_USER_HASH",
                                          "http_status_code": 400
                                        }"""
                        )
                )
        ),
})
public @interface QueryBallotBadRequestApiResponses {
}
