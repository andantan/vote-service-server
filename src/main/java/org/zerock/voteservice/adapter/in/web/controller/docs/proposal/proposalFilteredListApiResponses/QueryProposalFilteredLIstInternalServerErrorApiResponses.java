package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalFilteredListApiResponses;
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
                                        name = "서버 오류 예시",
                                        summary = "일반적인 서버 내부 오류",
                                        value = """
                                     {
                                       "success": false,
                                       "message": "알 수 없는 서버 오류가 발생했습니다.",
                                       "grpcServerStatus": "UNKNOWN_ERROR",
                                       "http_status_code": 500
                                     }"""
                                )
                        }
                )
        )
})
public @interface QueryProposalFilteredLIstInternalServerErrorApiResponses {
}
