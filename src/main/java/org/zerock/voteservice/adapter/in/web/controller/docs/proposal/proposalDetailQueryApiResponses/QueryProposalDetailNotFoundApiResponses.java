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
                responseCode = "404",
                description = "해당 투표를 찾을 수 없음",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = @ExampleObject(
                                name = "투표 없음 예시",
                                summary = "PROPOSAL_NOT_FOUND 오류",
                                value = """
                                        {
                                          "success": false,
                                          "message": "해당 투표를 찾을 수 없습니다.",
                                          "grpcServerStatus": "PROPOSAL_NOT_FOUND",
                                          "http_status_code": 404
                                        }"""
                        )
                )
        ),
})
public @interface QueryProposalDetailNotFoundApiResponses {
}
