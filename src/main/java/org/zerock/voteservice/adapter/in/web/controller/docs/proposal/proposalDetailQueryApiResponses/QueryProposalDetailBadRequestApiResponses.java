package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalDetailQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.domain.dto.query.error.QueryErrorResponseDto;

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
                            잘못된 요청 (예: 유효하지 않은 topic 형식)
                            
                             - topic이 null인 경우
                            """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "잘못된 요청 예시 1",
                                        summary = "topic이 null인 경우",
                                        value = """
                                        {
                                          "success": false,
                                          "message": "유효하지 않은 투표 이름입니다.",
                                          "grpcServerStatus": "INVALID_PARAMETER",
                                          "http_status_code": 400
                                        }"""
                                )
                        }
                )
        ),
})
public @interface QueryProposalDetailBadRequestApiResponses {
}
