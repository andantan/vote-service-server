package org.zerock.voteservice.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import org.zerock.voteservice.dto.vote.VoteProposalDto;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 관리", description = "투표 생성 관련 API")
@Operation(
        summary = "새로운 투표 제안 생성",
        description = "사용자가 새로운 투표를 제안하고 등록합니다. 이 과정은 제안 유효성 검사, 블록체인 등록 및 캐싱 단계를 거칩니다. 투표 주제, 기간(테스트: 분 단위) 등을 지정할 수 있습니다.",
        requestBody = @RequestBody(
                description = "새로운 투표 제안을 위한 요청 본문",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteProposalDto.class),
                        examples = @ExampleObject(
                                name = "투표 제안 요청 예시",
                                summary = "새로운 투표 생성",
                                value = """
                                    {
                                      "topic": "법률 개정안 찬반 투표",
                                      "duration": 24
                                    }"""
                        )
                )
        )
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "투표 제안 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표 등록 완료",
                                value = """
                                            {
                                              "duration": "1",
                                              "success": "true",
                                              "topic": "법률 개정안 찬반 투표",
                                              "message": "투표 등록이 완료되었습니다.",
                                              "status": "OK"
                                            }"""
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "투표 제안 실패 (예: 현재 진행 중인 투표, 마감된 투표 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = "실패 응답 예시 (이미 진행 중인 투표)",
                                        summary = "진행 중인 투표 오류",
                                        value = """
                                                    {
                                                      "duration": "1",
                                                      "success": "false",
                                                      "topic": "법률 개정안 찬반 투표",
                                                      "message": "현재 진행 중인 투표입니다.",
                                                      "status": "PROPOSAL_ALREADY_OPEN"
                                                    }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (이전에 진행했던 투표)",
                                        summary = "이전 투표 오류",
                                        value = """
                                                    {
                                                      "duration": "1",
                                                      "success": "false",
                                                      "topic": "법률 개정안 찬반 투표",
                                                      "message": "이미 진행되었던 투표입니다.",
                                                      "status": "PROPOSAL_EXPIRED"
                                                    }"""
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류로 인한 투표 제안 실패",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                                name = "서버 오류 예시",
                                summary = "내부 서버 오류",
                                value = """
                                            {
                                              "success": "false",
                                              "message": "알 수 없는 오류가 발생했습니다.",
                                              "status": "UNKNOWN_ERROR"
                                            }"""
                        )
                )
        )
})
public @interface VoteProposalApiDoc { }
