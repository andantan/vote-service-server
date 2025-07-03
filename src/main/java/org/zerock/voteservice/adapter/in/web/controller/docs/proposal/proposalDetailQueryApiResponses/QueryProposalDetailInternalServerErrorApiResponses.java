package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalDetailQueryApiResponses;

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
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "데이터베이스 접근 오류",
                                        summary = "DATABASE_ACCESS_ERROR 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "데이터베이스 서버에서 알 수 없는 오류가 발생했습니다.",
                                                  "status": "DATABASE_ACCESS_ERROR",
                                                  "http_status_code": 500
                                                }"""
                                ),
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
                                )
                        }
                )
        ),
})
public @interface QueryProposalDetailInternalServerErrorApiResponses {
}
