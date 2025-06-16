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

import org.zerock.voteservice.controller.docs.submitApiResponses.*;
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
        )
})
@SubmitBadRequestApiResponses
@SubmitNotFoundApiResponses
@SubmitNotAcceptableApiResponses
@SubmitConflictApiResponses
@SubmitInternalServerErrorApiResponses
public @interface VoteSubmitApiDoc { }
