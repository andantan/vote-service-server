package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalFilteredListApiResponses;

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
                        잘못된 요청 파라미터로 인해 투표 목록 조회 실패
                        - 에러 응답 인터페이스: [ QueryErrorResponseDto ] 참조
                        """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryErrorResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "잘못된 Limit 파라미터 예시",
                                        summary = "limit <= 0인 경우",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "요청된 Limit 값이 유효하지 않습니다. Limit은 0보다 커야 합니다.",
                                                  "grpcServerStatus": "LIMIT_ZERO_PARAM",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "잘못된 Skip 파라미터 예시",
                                        summary = "Skip < 0인 경우",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "요청된 Skip 값이 유효하지 않습니다. Skip은 0보다 커야 합니다.",
                                                  "grpcServerStatus": "SKIP_ZERO_PARAM",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "페이지 범위 초과 예시",
                                        summary = "조회 가능한 페이지 범위 초과한 경우",
                                        value = """
                                                {
                                                  "success": false,
                                                  "message": "조회 가능한 페이지 범위를 벗어났습니다.",
                                                  "grpcServerStatus": "PAGING_OUT_OF_BOUNDS",
                                                  "http_status_code": 400
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "잘못된 Sort 파라미터 예시",
                                        summary = "sortOrder이 잘못된 경우",
                                        value = """
                                        {
                                          "success": false,
                                          "message": "sortOrder 값은 'asc' 또는 'desc'만 가능합니다.",
                                          "grpcServerStatus": "INVALID_SORT_ORDER_PARAM",
                                          "http_status_code": 400
                                        }"""
                                ),
                                @ExampleObject(
                                        name = "잘못된 Sort 파라미터 세트 예시",
                                        summary = "sort 파라미터 셋이 잘못된 경우",
                                        value = """
                                        {
                                          "success": false,
                                          "message": "sortOrder와 sortBy 파라미터는 함께 사용하거나 모두 생략해야 합니다.",
                                          "grpcServerStatus": "INVALID_SORT_BY_PARAM",
                                          "http_status_code": 400
                                        }"""
                                )
                        }
                )
        ),
})
public @interface QueryProposalFilteredListBadRequestApiResponses {
}
