package org.zerock.voteservice.adapter.in.web.controller.query.docs.proposalDetailQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryProposalDetailResponseDto;

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
                        투표 조회 성공
                         - 투표 조회 응답 interface: [ QueryProposalDetailResponseDto ] 참조
                        """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = QueryProposalDetailResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "성공 응답 예시 (일반)",
                                        summary = "투표 정보 조회 성공",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "조회가 완료되었습니다.",
                                                  "status": "OK",
                                                  "http_status_code": 200,
                                                  "topic": "원격 근무 활성화를 위한 제도 개선",
                                                  "duration": 10,
                                                  "expired": true,
                                                  "block_heights": [
                                                    {
                                                      "height": "928",
                                                      "length": "1226"
                                                    },
                                                    {
                                                      "height": "936",
                                                      "length": "1637"
                                                    },
                                                    {
                                                      "height": "944",
                                                      "length": "2058"
                                                    },
                                                    {
                                                      "height": "949",
                                                      "length": "2348"
                                                    },
                                                    {
                                                      "height": "952",
                                                      "length": "4016"
                                                    }
                                                  ],
                                                  "result": {
                                                    "count": 11285,
                                                    "options": {
                                                      "1": 729,
                                                      "2": 729,
                                                      "3": 713,
                                                      "4": 760,
                                                      "5": 724
                                                    }
                                                  },
                                                  "created_at": "2025-06-14T18:25:44.378",
                                                  "expired_at": "2025-06-14T18:35:44.378",
                                                  "options": [
                                                    "1",
                                                    "2",
                                                    "3",
                                                    "4",
                                                    "5"
                                                  ]
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "성공 응답 예시 (진행 중 투표)",
                                        summary = "진행 중인 투표 조회 성공",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "조회가 완료되었습니다.",
                                                  "status": "OK",
                                                  "http_status_code": 200,
                                                  "topic": "기후 동행 카드 사용처 확대 찬반 투표",
                                                  "duration": 1,
                                                  "expired": false,
                                                  "block_heights": [],
                                                  "result": {
                                                    "count": 0,
                                                    "options": {}
                                                  },
                                                  "created_at": "2025-06-18T22:50:06.142",
                                                  "expired_at": "2025-06-18T22:51:06.142",
                                                  "options": [
                                                    "찬성",
                                                    "반대",
                                                    "기권"
                                                  ]
                                                }"""
                                )
                        }
                )
        ),
})
public @interface QueryProposalOkApiResponses {
}
