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

import org.zerock.voteservice.dto.vote.VoteBallotDto;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표지 관리", description = "투표지 제출 관련 API")
@Operation(
        summary = "새로운 투표지 제출",
        description = "사용자가 특정 투표에 대한 투표지를 제출합니다. 제출된 투표지는 유효성 검사, 블록체인 트랜잭션 등록 및 캐싱 단계를 거칩니다. 투표지 제출 후에는 재투표가 불가능합니다.",
        requestBody = @RequestBody(
                description = "투표지 제출을 위한 요청 본문",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteBallotDto.class),
                        examples = @ExampleObject(
                                name = "투표지 제출 요청 예시",
                                summary = "법률 개정안 투표",
                                value = """
                                            {
                                              "hash": "YOUR_USER_HASH",
                                              "topic": "법률 개정안 찬반 투표",
                                              "option": "찬성"
                                            }"""
                        )
                )
        )
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "투표지 등록 성공",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표지 등록 완료",
                                value = """
                                        {
                                          "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                          "success": "true",
                                          "vote_hash": "55ce7f2d9c198211e3f0bc58d7fe0c66827626cb1f9beb29bd650a8d5485c531",
                                          "topic": "법률 개정안 찬반 투표",
                                          "message": "투표 참여가 완료되었습니다.",
                                          "status": "OK",
                                          "option": "찬성"
                                        }"""
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "투표지 제출 실패 (예: 존재하지 않는 투표, 재투표 시도, 마감된 투표 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {
                                @ExampleObject(
                                        name = "실패 응답 예시 (진행 중이지 않은 투표)",
                                        summary = "투표가 진행 중이 아님",
                                        value = """
                                                {
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "success": "false",
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "현재 존재하지 않는 투표입니다.",
                                                  "status": "PROPOSAL_NOT_OPEN",
                                                  "option": "찬성"
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (재투표 시도)",
                                        summary = "중복 투표",
                                        value = """
                                                {
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "success": "false",
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "이미 참가한 투표입니다. (재투표 불가)",
                                                  "status": "DUPLICATE_VOTE_SUBMISSION",
                                                  "option": "찬성"
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (투표 마감)",
                                        summary = "투표 마감",
                                        value = """
                                                {
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "success": "false",
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "투표가 마감되어 정산 중입니다.",
                                                  "status": "TIMEOUT_PROPOSAL",
                                                  "option": "찬성"
                                                }"""
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류로 인한 투표지 등록 실패",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = @ExampleObject(
                                name = "서버 오류 예시",
                                summary = "내부 서버 오류",
                                value =  """
                                            {
                                              "success": "false",
                                              "message": "알수 없는 요류",
                                              "status": "UNKNOWN_ERROR"
                                            }"""
                        )
                )
        )
})
public @interface VoteSubmitApiDoc { }
