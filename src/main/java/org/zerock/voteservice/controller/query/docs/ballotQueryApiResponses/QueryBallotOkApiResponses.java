package org.zerock.voteservice.controller.query.docs.ballotQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.dto.query.BallotQueryResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = """
                            투표 기록 조회 성공 또는 기록 없음
                             - submitted_at: Date 형식에 따름
                            """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BallotQueryResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "성공 응답 예시 (기록 있음)",
                                        summary = "사용자 투표 기록 조회 성공 1",
                                        value = """
                                            {
                                              "success": true,
                                              "message": "조회가 완료되었습니다.",
                                              "status": "OK",
                                              "http_status_code": 200,
                                              "ballots": [
                                                {
                                                  "vote_hash": "d9b3614a683bb6a9ca99246497fbaa4805ddede48b8805d95995196fa3c7a0d4",
                                                  "topic": "공무원 연금 개혁안 타당성",
                                                  "submitted_at": "2025-06-14T16:59:44.817"
                                                },
                                                {
                                                  "vote_hash": "47225b1dc6acaf87e910e6f333683e121ec16e133dcac2135e235784fc0b9fe7",
                                                  "topic": "대기업의 사회적 책임 강화",
                                                  "submitted_at": "2025-06-14T17:05:01.162"
                                                }
                                              ],
                                              "ballot_length": 2
                                            }"""
                                ),
                                @ExampleObject(
                                        name = "성공 응답 예시 (기록 없음)",
                                        summary = "사용자 투표 기록 조회 성공 2",
                                        value = """
                                            {
                                              "success": true,
                                              "message": "조회가 완료되었습니다.",
                                              "status": "OK",
                                              "http_status_code": 200,
                                              "ballots": [],
                                              "ballot_length": 0
                                            }"""
                                )
                        }
                )
        )
})
public @interface QueryBallotOkApiResponses {
}
