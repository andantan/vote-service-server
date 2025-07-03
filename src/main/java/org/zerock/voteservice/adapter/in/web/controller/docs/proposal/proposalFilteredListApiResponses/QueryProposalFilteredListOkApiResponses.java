package org.zerock.voteservice.adapter.in.web.controller.docs.proposal.proposalFilteredListApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.zerock.voteservice.adapter.in.web.dto.QueryProposalFilteredListResponseDto;

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
                        투표 목록 조회 성공
                         - 투표 목록 조회 응답 인터페이스: [ QueryProposalFilteredListResponseDto ] 참조
                        """,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = QueryProposalFilteredListResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "성공 응답 예시 (일반 상세 조회)",
                                        summary = "투표 목록 상세 정보 조회 성공 (summarize=false)",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "조회가 완료되었습니다.",
                                                  "grpcServerStatus": "OK",
                                                  "http_status_code": 200,
                                                  "summarize": false,
                                                  "expired": true,
                                                  "sort_order": "desc",
                                                  "sort_by": "expiredAt",
                                                  "skip": 4,
                                                  "limit": 2,
                                                  "proposal_list": [
                                                    {
                                                      "topic": "AI 윤리 기준 마련의 시급성",
                                                      "duration": 8,
                                                      "expired": true,
                                                      "block_heights": [
                                                        {
                                                          "height": 540,
                                                          "length": 743
                                                        },
                                                        {
                                                          "height": 560,
                                                          "length": 915
                                                        },
                                                        {
                                                          "height": 573,
                                                          "length": 1256
                                                        },
                                                        {
                                                          "height": 583,
                                                          "length": 1836
                                                        }
                                                      ],
                                                      "result": {
                                                        "count": 4750,
                                                        "options": {
                                                          "1": 878,
                                                          "2": 966,
                                                          "3": 966,
                                                          "4": 968,
                                                          "5": 972
                                                        }
                                                      },
                                                      "created_at": "2025-06-14T16:59:10.832",
                                                      "expired_at": "2025-06-14T17:07:10.832",
                                                      "options": [
                                                        "1",
                                                        "2",
                                                        "3",
                                                        "4",
                                                        "5"
                                                      ]
                                                    },
                                                    {
                                                      "topic": "농촌 소멸 위기 극복을 위한 귀농 정책",
                                                      "duration": 2,
                                                      "expired": true,
                                                      "block_heights": [
                                                        {
                                                          "height": 541,
                                                          "length": 740
                                                        }
                                                      ],
                                                      "result": {
                                                        "count": 740,
                                                        "options": {
                                                          "1": 131,
                                                          "2": 168,
                                                          "3": 139,
                                                          "4": 148,
                                                          "5": 154
                                                        }
                                                      },
                                                      "created_at": "2025-06-14T16:59:10.834",
                                                      "expired_at": "2025-06-14T17:01:10.834",
                                                      "options": [
                                                        "1",
                                                        "2",
                                                        "3",
                                                        "4",
                                                        "5"
                                                      ]
                                                    }
                                                  ],
                                                  "proposal_list_length": 2
                                                }"""
                                ),
                                @ExampleObject(
                                        name = "성공 응답 예시 (요약 조회)",
                                        summary = "투표 목록 요약 정보 조회 성공 (summarize=true)",
                                        value = """
                                                {
                                                  "success": true,
                                                  "message": "조회가 완료되었습니다.",
                                                  "grpcServerStatus": "OK",
                                                  "http_status_code": 200,
                                                  "summarize": true,
                                                  "expired": true,
                                                  "sort_order": "desc",
                                                  "sort_by": "expiredAt",
                                                  "skip": 10,
                                                  "limit": 10,
                                                  "proposal_list": [
                                                    {
                                                      "topic": "동물 복지법 강화 찬반 투표",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:02:10.839"
                                                    },
                                                    {
                                                      "topic": "학교 폭력 예방을 위한 교육 강화",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:02:10.84"
                                                    },
                                                    {
                                                      "topic": "정부 예산 효율성 평가",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:08:10.824"
                                                    },
                                                    {
                                                      "topic": "수원시 대중교통 만족도 평가",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:05:10.899"
                                                    },
                                                    {
                                                      "topic": "농수산물 가격 안정화 대책",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:03:10.903"
                                                    },
                                                    {
                                                      "topic": "우주 산업 육성을 위한 국가 전략",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:07:10.906"
                                                    },
                                                    {
                                                      "topic": "친환경 에너지 전환 정책 평가",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:08:10.909"
                                                    },
                                                    {
                                                      "topic": "지역 경제 활성화 방안 제안",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:00:10.911"
                                                    },
                                                    {
                                                      "topic": "공무원 연금 개혁안 타당성",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:07:10.913"
                                                    },
                                                    {
                                                      "topic": "지방자치단체 재정 자립도 강화",
                                                      "expired": true,
                                                      "expired_at": "2025-06-14T17:03:10.916"
                                                    }
                                                  ],
                                                  "proposal_list_length": 10
                                                }"""
                                )
                        }
                )
        ),
})
public @interface QueryProposalFilteredListOkApiResponses {
}