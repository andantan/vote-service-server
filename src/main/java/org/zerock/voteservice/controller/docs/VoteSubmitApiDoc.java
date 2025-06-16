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

import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.dto.vote.VoteBallotResponseDto;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "투표지 관리", description = "투표지 제출 관련 API")
@Operation(
        summary = "새로운 투표지 제출",
        description = """
                    새로운 투표지 생성 및 등록\n
                    - 투표지 등록 요청 interface: Schemas [ VoteBallotRequestDto ] 참조\n
                    - 투표지 등록 응답 interface: Schemas [ VoteBallotResponseDto ] 참조
                    """,
        requestBody = @RequestBody(
                description = "투표지 제출을 위한 요청 Body",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteBallotRequestDto.class),
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
                        schema = @Schema(implementation = VoteBallotResponseDto.class),
                        examples = @ExampleObject(
                                name = "성공 응답 예시",
                                summary = "투표지 등록 완료",
                                value = """
                                        {
                                          "success": true,
                                          "topic": "법률 개정안 찬반 투표",
                                          "message": "투표 참여가 완료되었습니다.",
                                          "status": "OK",
                                          "http_status_code": 200,
                                          "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                          "vote_option": "찬성",
                                          "vote_hash": "aeb4b82ecad02ae8f76762ebeaf5335a121f25501ba0302c77367b663b70e8b3"
                                        }"""
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "투표지 제출 실패 (예: 존재하지 않는 투표, 재투표 시도, 마감된 투표 등)",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = VoteBallotResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "실패 응답 예시 (진행 중이지 않은 투표)",
                                        summary = "투표가 진행 중이 아님",
                                        value = """
                                                {
                                                  "success": false,
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "현재 존재하지 않는 투표입니다.",
                                                  "status": "PROPOSAL_NOT_OPEN",
                                                  "http_status_code": 404,
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "vote_option": "찬성",
                                                  "vote_hash": "0000000000000000000000000000000000000000000000000000000000000000"
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (재투표 시도)",
                                        summary = "중복 투표",
                                        value = """
                                                {
                                                  "success": false,
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "이미 참가한 투표입니다. (재투표 불가)",
                                                  "status": "DUPLICATE_VOTE_SUBMISSION",
                                                  "http_status_code": 409,
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "vote_option": "찬성",
                                                  "vote_hash": "0000000000000000000000000000000000000000000000000000000000000000"
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "실패 응답 예시 (투표 마감)",
                                        summary = "투표 마감",
                                        value = """
                                                {
                                                  "success": false,
                                                  "topic": "법률 개정안 찬반 투표",
                                                  "message": "투표가 마감되어 정산 중입니다.",
                                                  "status": "TIMEOUT_PROPOSAL",
                                                  "http_status_code": 406,
                                                  "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                                  "vote_option": "찬성",
                                                  "vote_hash": "0000000000000000000000000000000000000000000000000000000000000000"
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
                        schema = @Schema(implementation = VoteBallotResponseDto.class),
                        examples = @ExampleObject(
                                name = "서버 오류 예시",
                                summary = "내부 서버 오류",
                                value =  """
                                        {
                                          "success": false,
                                          "topic": "법률 개정안 찬반 투표",
                                          "message": "알 수 없는 오류",
                                          "status": "UNKNOWN_ERROR",
                                          "http_status_code": 500,
                                          "user_hash": "1303522e0b59179b948c1713105ac8f9d13c62080a743118617fecef799e92e5",
                                          "vote_option": "찬성",
                                          "vote_hash": "0000000000000000000000000000000000000000000000000000000000000000"
                                        }"""
                        )
                )
        )
})
public @interface VoteSubmitApiDoc { }
