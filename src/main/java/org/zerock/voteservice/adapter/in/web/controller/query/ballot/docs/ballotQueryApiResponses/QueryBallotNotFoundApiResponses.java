package org.zerock.voteservice.adapter.in.web.controller.query.ballot.docs.ballotQueryApiResponses;

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
                responseCode = "404",
                description = "요청한 유저를 찾을 수 없음",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "잘못된 요청 예시",
                                summary = "유효하지 않은 userHash",
                                value = """
                                        {
                                          "success": false,
                                          "message": "해당 유권자를 찾을 수 없습니다.",
                                          "status": "USER_NOT_FOUND",
                                          "http_status_code": 404
                                        }"""
                        )
                )
        ),
})
public @interface QueryBallotNotFoundApiResponses {
}
