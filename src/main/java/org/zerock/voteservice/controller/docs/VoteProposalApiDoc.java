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

import org.zerock.voteservice.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.dto.vote.VoteProposalResponseDto;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표 관리", description = "투표 생성 관련 API")
@Operation(
        summary = "새로운 투표 제안 생성",
        description = """
                    새로운 투표 제안 및 등록\n
                    - 투표 생성 요청 interface: Schemas [ VoteProposalRequestDto ] 참조\n
                    - 투표 생성 응답 interface: Schemas [ VoteProposalResponseDto ] 참조
                    """,
        requestBody = @RequestBody(
                description = "새로운 투표 제안을 위한 요청 Body",
                required = true,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteProposalRequestDto.class),
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
                        schema = @Schema(implementation = VoteProposalResponseDto.class),
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표 등록 완료",
                                value = """
                                        {
                                          "success": true,
                                          "topic": "법률 개정안 찬반 투표",
                                          "duration": 60,
                                          "message": "투표 등록이 완료되었습니다.",
                                          "status": "OK",
                                          "http_status_code": 200
                                        }"""
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "투표 제안 실패 (예: 현재 진행 중인 투표, 마감된 투표 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteProposalResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "실패 응답 예시 (이미 진행 중인 투표)",
                                        summary = "진행 중인 투표 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "duration": 60,
                                                  "message": "현재 진행 중인 투표입니다.",
                                                  "status": "PROPOSAL_ALREADY_OPEN",
                                                  "http_status_code": 409
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (이전에 진행했던 투표)",
                                        summary = "이전 투표 오류",
                                        value = """
                                                {
                                                  "success": false,
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "duration": 60,
                                                  "message": "이미 진행되었던 투표입니다.",
                                                  "status": "PROPOSAL_EXPIRED",
                                                  "http_status_code": 400
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
                        schema = @Schema(implementation = VoteProposalResponseDto.class),
                        examples = @ExampleObject(
                                name = "서버 오류 예시",
                                summary = "내부 서버 오류",
                                value = """
                                        {
                                          "success": false,
                                          "topic": "법률 개정안 찬반 투표",
                                          "duration": 60,
                                          "message": "알 수 없는 오류가 발생했습니다.",
                                          "status": "UNKNOWN_ERROR",
                                          "http_status_code": 500
                                        }"""
                        )
                )
        )
})
public @interface VoteProposalApiDoc { }
